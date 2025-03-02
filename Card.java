import java.util.*;
import java.text.SimpleDateFormat;

// อินเตอร์เฟซสำหรับบันทึกการใช้งานบัตร
interface CardUsageLogger {
    void recordUsage(String cardId, String location, boolean accessGranted, long usageTime);
    void recordCardChange(String cardId, String changeDetails, long changeTime);
}

// คลาสบัตรเบื้องต้น (Abstract Card Class)
abstract class Card {
    protected String cardId;
    protected String ownerName;
    protected String accessLevel;
    protected int usageCount = 0;
    protected boolean isActive = true;
    protected long lastUsedTime;
    protected List<String> accessHistory = new ArrayList<>();
    protected long validFrom;
    protected long validUntil;

    public Card(String ownerName, String accessLevel, long validFrom, long validUntil) {
        this.cardId = generateCardId();
        this.ownerName = ownerName;
        this.accessLevel = accessLevel;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    private String generateCardId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void grantAccess(Room room, CardUsageLogger logger) {
        if (!isActive) {
            System.out.println("บัตรถูกระงับการใช้งาน: " + cardId);
            return;
        }

        // ตรวจสอบว่าเวลาของบัตรยังไม่หมดอายุ
        long currentTime = System.currentTimeMillis();
        if (currentTime < validFrom || currentTime > validUntil) {
            System.out.println("บัตรหมดอายุหรือไม่สามารถใช้งานในขณะนี้: " + cardId);
            return;
        }

        boolean accessGranted = room.canAccess(this);
        lastUsedTime = currentTime;

        if (accessGranted) {
            usageCount++;
            System.out.println(ownerName + " ได้รับอนุญาตให้เข้าห้อง " + room.getRoomName());
            accessHistory.add("เข้าถึงห้อง " + room.getRoomName() + " เวลา " + new Date(lastUsedTime));
        } else {
            System.out.println(ownerName + " ไม่ได้รับอนุญาตให้เข้าห้อง " + room.getRoomName());
        }

        // บันทึกการใช้งาน
        logger.recordUsage(cardId, room.getRoomName(), accessGranted, lastUsedTime);
    }


    public void deactivateCard() {
        isActive = false;
        System.out.println("บัตร " + cardId + " ถูกปิดการใช้งานแล้ว.");
    }

    public String getCardId() {
        return cardId;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void printAccessHistory() {
        System.out.println("ประวัติการใช้งานของบัตร " + cardId + ":");
        for (String record : accessHistory) {
            System.out.println(record);
        }
    }

    @Override
    public String toString() {
        return "บัตร[ID: " + cardId + ", เจ้าของ: " + ownerName + ", สิทธิ์: " + accessLevel + "]";
    }

    public void updateCardInfo(String newOwnerName, String newAccessLevel, CardUsageLogger logger) {
        if (!this.ownerName.equals(newOwnerName)) {
            String changeDetails = "เจ้าของบัตรเปลี่ยนจาก " + this.ownerName + " เป็น " + newOwnerName;
            this.ownerName = newOwnerName;
            logger.recordCardChange(cardId, changeDetails, System.currentTimeMillis());
        }
        if (!this.accessLevel.equals(newAccessLevel)) {
            String changeDetails = "สิทธิ์บัตรเปลี่ยนจาก " + this.accessLevel + " เป็น " + newAccessLevel;
            this.accessLevel = newAccessLevel;
            logger.recordCardChange(cardId, changeDetails, System.currentTimeMillis());
        }
        System.out.println("ข้อมูลบัตร " + cardId + " ถูกอัพเดต.");
    }
}

// คลาสสำหรับการจัดการการ์ด
class CardManager {
    private Map<String, Card> cardDatabase = new HashMap<>();

    public void addCard(Card card) {
        cardDatabase.put(card.getCardId(), card);
    }

    public void listCards() {
        if (cardDatabase.isEmpty()) {
            System.out.println("ไม่มีบัตรในระบบ!\n");
            return;
        }
        for (Card card : cardDatabase.values()) {
            System.out.println(card);
        }
    }

    public Card getCard(String cardId) {
        return cardDatabase.get(cardId);
    }

    public void reportLost(String cardId) {
        Card card = cardDatabase.get(cardId);
        if (card != null) {
            card.deactivateCard();
        }
    }

    public void updateCardInfo(String cardId, String newOwnerName, String newAccessLevel, CardUsageLogger logger) {
        Card card = cardDatabase.get(cardId);
        if (card == null) {
            System.out.println("ไม่พบบัตรนี้!\n");
            return;
        }

        // เรียกเมธอดใน Card สำหรับการอัพเดต
        card.updateCardInfo(newOwnerName, newAccessLevel, logger);
    }
}

// การ์ดสำหรับประเภทต่างๆ
class CustomerCard extends Card {
    public CustomerCard(String ownerName, long validFrom, long validUntil) {
        super(ownerName, "Customer", validFrom, validUntil);
    }
}

class GuestCard extends Card {
    public GuestCard(String ownerName, long validFrom, long validUntil) {
        super(ownerName, "Guest", validFrom, validUntil);
    }
}

class EmployeeCard extends Card {
    public EmployeeCard(String ownerName, long validFrom, long validUntil) {
        super(ownerName, "Employee", validFrom, validUntil);
    }
}

class HousekeeperCard extends Card {
    public HousekeeperCard(String ownerName, long validFrom, long validUntil) {
        super(ownerName, "Housekeeper", validFrom, validUntil);
    }
}

class SecurityCard extends Card {
    public SecurityCard(String ownerName, long validFrom, long validUntil) {
        super(ownerName, "Security", validFrom, validUntil);
    }
}

// คลาส AccessLogger ที่จะบันทึกการใช้งานและการเปลี่ยนแปลงข้อมูล
class AccessLogger implements CardUsageLogger {
    private List<CardUsageRecord> usageRecords = new ArrayList<>();
    private List<CardChangeRecord> changeRecords = new ArrayList<>();

    // บันทึกการใช้งานบัตร
    @Override
    public void recordUsage(String cardId, String location, boolean accessGranted, long usageTime) {
        CardUsageRecord record = new CardUsageRecord(cardId, location, accessGranted, usageTime);
        usageRecords.add(record);
        System.out.println(record);
    }

    // บันทึกการเปลี่ยนแปลงข้อมูลบัตร
    @Override
    public void recordCardChange(String cardId, String changeDetails, long changeTime) {
        CardChangeRecord changeRecord = new CardChangeRecord(cardId, changeDetails, changeTime);
        changeRecords.add(changeRecord);
        System.out.println(changeRecord);
    }

    // เมธอดแสดงการใช้งานทั้งหมด
    public void printAllUsage() {
        System.out.println("\n===== รายการการใช้งานทั้งหมด =====");
        for (CardUsageRecord record : usageRecords) {
            System.out.println(record);
        }
    }

    // เมธอดแสดงการเปลี่ยนแปลงข้อมูลทั้งหมด
    public void printAllChanges() {
        System.out.println("\n===== รายการการเปลี่ยนแปลงข้อมูลบัตร =====");
        for (CardChangeRecord changeRecord : changeRecords) {
            System.out.println(changeRecord);
        }
    }

    // คลาสสำหรับเก็บข้อมูลการใช้งานบัตร
    class CardUsageRecord {
        private String cardId;
        private String location;
        private boolean accessGranted;
        private long usageTime;

        public CardUsageRecord(String cardId, String location, boolean accessGranted, long usageTime) {
            this.cardId = cardId;
            this.location = location;
            this.accessGranted = accessGranted;
            this.usageTime = usageTime;
        }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy 'เวลา' HH:mm:ss", new Locale("th", "TH"));
            String formattedDate = dateFormat.format(new Date(usageTime));
            return "บัตร " + cardId + " เข้า " + location + " - " + (accessGranted ? "อนุญาต" : "ไม่อนุญาต") + " เวลา: " + formattedDate;
        }
    }

    // คลาสสำหรับเก็บข้อมูลการเปลี่ยนแปลงข้อมูลบัตร
    class CardChangeRecord {
        private String cardId;
        private String changeDetails;
        private long changeTime;

        public CardChangeRecord(String cardId, String changeDetails, long changeTime) {
            this.cardId = cardId;
            this.changeDetails = changeDetails;
            this.changeTime = changeTime;
        }

        @Override
        public String toString() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy 'เวลา' HH:mm:ss", new Locale("th", "TH"));
            String formattedDate = dateFormat.format(new Date(changeTime));
            return "บัตร " + cardId + " การเปลี่ยนแปลง: " + changeDetails + " เวลา: " + formattedDate;
        }
    }
}