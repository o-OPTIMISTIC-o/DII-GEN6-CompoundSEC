import java.util.*;

// Room/Floor Management
class Room {
    private String roomName;
    private String floor;
    private String requiredAccessLevel;

    public Room(String roomName, String floor, String requiredAccessLevel) {
        this.roomName = roomName;
        this.floor = floor;
        this.requiredAccessLevel = requiredAccessLevel;
    }

    public boolean canAccess(Card card) {
        return card.accessLevel.equals(requiredAccessLevel);
    }

    public String getRoomName() {
        return roomName;
    }
}

// Lost and Stolen Card Management
class CardManager {
    private Map<String, Card> cardRegistry = new HashMap<>();

    public void registerCard(Card card) {
        cardRegistry.put(card.cardID, card);
    }

    public void reportLost(String cardID) {
        if (cardRegistry.containsKey(cardID)) {
            cardRegistry.get(cardID).deactivateCard();
        } else {
            System.out.println("Card not found.");
        }
    }
}