import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// อินเตอร์เฟซสำหรับบันทึกการใช้งานบัตร
interface CardUsageLogger {
    void recordUsage(String cardId, String location, boolean accessGranted, long usageTime);
}

// คลาสบัตรเบื้องต้น (Abstract Card Class)
abstract class Card {
    protected String cardId;
    protected String ownerName;
    protected String accessLevel;
    protected int usageCount = 0;
    protected boolean isActive = true;
    protected long lastUsedTime;

    public Card(String ownerName, String accessLevel) {
        this.cardId = generateCardId();
        this.ownerName = ownerName;
        this.accessLevel = accessLevel;
    }

    private String generateCardId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void grantAccess(Room room, CardUsageLogger logger) {
        boolean accessGranted = room.canAccess(this);

        if (isActive && accessGranted) {
            usageCount++;
            lastUsedTime = System.currentTimeMillis();
            System.out.println(ownerName + " ได้รับอนุญาตให้เข้าห้อง " + room.getRoomName());
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

    @Override
    public String toString() {
        return "บัตร[ID: " + cardId + ", เจ้าของ: " + ownerName + ", สิทธิ์: " + accessLevel + "]";
    }
}

// บัตรประเภทต่างๆ
class CustomerCard extends Card {
    public CustomerCard(String ownerName) {
        super(ownerName, "Customer");
    }
}

class GuestCard extends Card {
    public GuestCard(String ownerName) {
        super(ownerName, "Guest");
    }
}

class EmployeeCard extends Card {
    public EmployeeCard(String ownerName) {
        super(ownerName, "Employee");
    }
}

class HousekeeperCard extends Card {
    public HousekeeperCard(String ownerName) {
        super(ownerName, "Housekeeper");
    }
}

class SecurityCard extends Card {
    public SecurityCard(String ownerName) {
        super(ownerName, "Security");
    }
}

// การจัดการบัตร
class CardManager {
    private Map<String, Card> cardRegistry = new HashMap<>();

    public void addCard(Card card) {
        cardRegistry.put(card.getCardId(), card);
    }

    public Card getCard(String cardId) {
        return cardRegistry.get(cardId);
    }

    public void listCards() {
        for (Card card : cardRegistry.values()) {
            System.out.println(card);
        }
    }

    public void reportLost(String cardId) {
        if (cardRegistry.containsKey(cardId)) {
            cardRegistry.get(cardId).deactivateCard();
        } else {
            System.out.println("ไม่พบบัตรนี้.");
        }
    }
}

// คลาสสำหรับบันทึกการใช้งานบัตร
class AccessLogger implements CardUsageLogger {
    @Override
    public void recordUsage(String cardId, String location, boolean accessGranted, long usageTime) {
        // สร้างตัวแปร SimpleDateFormat สำหรับแปลงเวลาจาก timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy 'เวลา' HH:mm:ss", new Locale("th", "TH"));
        String formattedDate = dateFormat.format(new Date(usageTime));  // แปลงเวลา timestamp เป็นรูปแบบที่อ่านง่าย

        System.out.println("บันทึก: บัตร " + cardId + " เข้า " + location + " - " + (accessGranted ? "อนุญาต" : "ไม่อนุญาต") + " เวลา: " + formattedDate);
    }
}
