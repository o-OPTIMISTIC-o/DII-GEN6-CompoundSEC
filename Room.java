
// ห้องประเภทต่างๆ
abstract class Room {
    protected String roomName;
    protected String requiredAccessLevel;

    public Room(String roomName, String requiredAccessLevel) {
        this.roomName = roomName;
        this.requiredAccessLevel = requiredAccessLevel;
    }

    public boolean canAccess(Card card) {
        return card.getAccessLevel().equals(requiredAccessLevel);
    }

    public String getRoomName() {
        return roomName;
    }
}

class RelaxationRoom extends Room {
    public RelaxationRoom(String roomName) {
        super(roomName, "Customer");
    }
}

class SeminarRoom extends Room {
    public SeminarRoom(String roomName) {
        super(roomName, "Guest");
    }
}

class EmployeeRoom extends Room {
    public EmployeeRoom(String roomName) {
        super(roomName, "Employee");
    }
}

class HousekeeperRoom extends Room {
    public HousekeeperRoom(String roomName) {
        super(roomName, "Housekeeper");
    }
}

class SecurityRoom extends Room {
    public SecurityRoom(String roomName) {
        super(roomName, "Security");
    }
}