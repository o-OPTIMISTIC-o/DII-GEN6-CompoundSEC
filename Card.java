import java.util.*;

// Interface สำหรับบันทึกการใช้งานบัตร
interface CardUsageLogger {
    void recordUsage(String cardId, String location, boolean accessGranted);
}

// Abstract Card Class
abstract class Card {
    protected String cardID;
    protected String ownerName;
    protected String accessLevel;
    protected int usageCount = 0;
    protected boolean isActive = true;

    public Card(String cardID, String ownerName, String accessLevel) {
        this.cardID = cardID;
        this.ownerName = ownerName;
        this.accessLevel = accessLevel;
    }

    public abstract void grantAccess(String location);

    public void deactivateCard() {
        isActive = false;
        System.out.println("Card " + cardID + " is deactivated.");
    }

    public void activateCard() {
        isActive = true;
        System.out.println("Card " + cardID + " is activated.");
    }

    public String getCardType() {
        return this.getClass().getSimpleName();
    }
}

// Customer Card
class CustomerCard extends Card {
    public CustomerCard(String cardID, String ownerName, String accessLevel) {
        super(cardID, ownerName, accessLevel);
    }

    @Override
    public void grantAccess(String location) {
        if (isActive) {
            usageCount++;
            System.out.println("Customer " + ownerName + " is accessing " + location);
        } else {
            System.out.println("Card is deactivated.");
        }
    }
}

// Employee Card
class EmployeeCard extends Card {
    public EmployeeCard(String cardID, String ownerName, String accessLevel) {
        super(cardID, ownerName, accessLevel);
    }

    @Override
    public void grantAccess(String location) {
        if (isActive) {
            usageCount++;
            System.out.println("Employee " + ownerName + " is accessing " + location);
        } else {
            System.out.println("Card is deactivated.");
        }
    }
}

// Class สำหรับการบันทึกการใช้บัตร
class AccessLogger implements CardUsageLogger {
    @Override
    public void recordUsage(String cardId, String location, boolean accessGranted) {
        System.out.println("Logging: Card " + cardId + " attempted access at " + location + " - " + (accessGranted ? "GRANTED" : "DENIED"));
    }
}
