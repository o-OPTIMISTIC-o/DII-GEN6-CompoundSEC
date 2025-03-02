import java.util.*;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CardManager cardManager = new CardManager();
        AccessLogger accessLogger = new AccessLogger();

        List<Room> rooms = new ArrayList<>();
        rooms.add(new RelaxationRoom("Relaxation Zone"));
        rooms.add(new SeminarRoom("Seminar Hall A"));
        rooms.add(new EmployeeRoom("Staff Lounge"));
        rooms.add(new HousekeeperRoom("Housekeeper Storage"));
        rooms.add(new SecurityRoom("Security Office"));

        while (true) {
            System.out.println("=============================================================================================================");
            System.out.println("\n1. เพิ่มบัตร");
            System.out.println("2. แสดงบัตรทั้งหมด");
            System.out.println("3. ตรวจสอบสิทธิ์เข้าห้อง");
            System.out.println("4. รายงานบัตรหาย");
            System.out.println("5. แสดงประวัติการใช้งานบัตรทั้งหมด");
            System.out.println("6. แสดงประวัติการเปลี่ยนแปลงข้อมูลบัตรทั้งหมด");
            System.out.println("7. แก้ไขข้อมูลบัตร");
            System.out.println("8. ออกจากระบบ");
            System.out.print("เลือกเมนู: ");

            if (!scanner.hasNextInt()) {
                System.out.println("กรุณาป้อนตัวเลขที่ถูกต้อง!\n");
                scanner.next();
                continue;
            }
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // การเพิ่มบัตร
                    System.out.print("ชื่อเจ้าของบัตร: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("ประเภทบัตร (Customer / Guest / Employee / Housekeeper / Security): ");
                    String level = scanner.nextLine().trim().toLowerCase();
                    System.out.print("วันที่เริ่มใช้ (รูปแบบ yyyy-MM-dd): ");
                    String validFromStr = scanner.nextLine().trim();
                    System.out.print("วันที่หมดอายุ (รูปแบบ yyyy-MM-dd): ");
                    String validUntilStr = scanner.nextLine().trim();

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date validFromDate = sdf.parse(validFromStr);
                        Date validUntilDate = sdf.parse(validUntilStr);
                        long validFrom = validFromDate.getTime();
                        long validUntil = validUntilDate.getTime();

                        Card card = null;
                        switch (level) {
                            case "customer":
                                card = new CustomerCard(name, validFrom, validUntil);
                                break;
                            case "guest":
                                card = new GuestCard(name, validFrom, validUntil);
                                break;
                            case "employee":
                                card = new EmployeeCard(name, validFrom, validUntil);
                                break;
                            case "housekeeper":
                                card = new HousekeeperCard(name, validFrom, validUntil);
                                break;
                            case "security":
                                card = new SecurityCard(name, validFrom, validUntil);
                                break;
                            default:
                                System.out.println("ประเภทบัตรไม่ถูกต้อง!\n");
                                continue;
                        }

                        cardManager.addCard(card);
                        System.out.println("เพิ่มบัตร: " + card);

                    } catch (Exception e) {
                        System.out.println("กรุณากรอกวันที่ในรูปแบบที่ถูกต้อง (yyyy-MM-dd)\n");
                    }
                    break;
                case 2:
                    cardManager.listCards();
                    break;

                case 3:
                    System.out.print("กรอกรหัสบัตร: ");
                    String cardId = scanner.nextLine().trim();
                    Card selectedCard = cardManager.getCard(cardId);

                    if (selectedCard == null) {
                        System.out.println("ไม่พบบัตรนี้!\n");
                        continue;
                    }

                    for (int i = 0; i < rooms.size(); i++) {
                        System.out.println((i + 1) + ". " + rooms.get(i).getRoomName());
                    }
                    System.out.print("เลือกห้องที่ต้องการเข้า (หมายเลข): ");

                    if (!scanner.hasNextInt()) {
                        System.out.println("กรุณาป้อนหมายเลขห้องที่ถูกต้อง!\n");
                        scanner.next();
                        continue;
                    }
                    int roomChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (roomChoice < 1 || roomChoice > rooms.size()) {
                        System.out.println("หมายเลขห้องไม่ถูกต้อง!\n");
                        continue;
                    }
                    selectedCard.grantAccess(rooms.get(roomChoice - 1), accessLogger);
                    break;

                case 4:
                    System.out.print("กรอกรหัสบัตรที่หาย: ");
                    String lostCardId = scanner.nextLine().trim();
                    if (cardManager.getCard(lostCardId) == null) {
                        System.out.println("ไม่พบบัตรนี้!\n");
                    } else {
                        cardManager.reportLost(lostCardId);
                    }
                    break;

                case 5:
                    accessLogger.printAllUsage();
                    break;

                case 6:
                    accessLogger.printAllChanges();
                    break;

                case 7:
                    System.out.print("กรอกรหัสบัตรที่ต้องการแก้ไข: ");
                    cardId = scanner.nextLine().trim();
                    selectedCard = cardManager.getCard(cardId);

                    if (selectedCard == null) {
                        System.out.println("ไม่พบบัตรนี้!\n");
                        continue;
                    }

                    System.out.print("กรุณากรอกชื่อเจ้าของบัตรใหม่: ");
                    String newOwnerName = scanner.nextLine().trim();
                    System.out.print("กรุณากรอกสิทธิ์การเข้าถึงใหม่ (Customer / Guest / Employee / Housekeeper / Security): ");
                    String newAccessLevel = scanner.nextLine().trim();

                    // อัพเดตข้อมูลบัตร
                    cardManager.updateCardInfo(cardId, newOwnerName, newAccessLevel, accessLogger);
                    break;

                case 8:
                    System.out.println("\nออกจากระบบ...\n");
                    scanner.close();
                    return;

                default:
                    System.out.println("ตัวเลือกไม่ถูกต้อง! กรุณาเลือก 1-7\n");
            }
        }
    }
}