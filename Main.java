// Main Execution
public class Main {
    public static void main(String[] args) {
        Card customer = new CustomerCard("C123", "Ashley", "Guest");
        Card employee = new EmployeeCard("E456", "Bob", "Manager");
        Room seminarRoom = new Room("Seminar Hall A", "2nd Floor", "Manager");

        AccessLogger logger = new AccessLogger();

        // ทดสอบการเข้าใช้งานห้อง
        if (seminarRoom.canAccess(customer)) {
            customer.grantAccess(seminarRoom.getRoomName());
            logger.recordUsage(customer.cardID, seminarRoom.getRoomName(), true);
        } else {
            System.out.println("Access Denied for " + customer.ownerName);
            logger.recordUsage(customer.cardID, seminarRoom.getRoomName(), false);
        }
    }
}