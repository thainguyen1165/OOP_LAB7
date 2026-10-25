import java.util.List;
import java.util.Set;

public class Main {
    private static int passed;

    public static void main(String[] args) {
        IRepository<ProjectorRemote> projectors = new InMemoryRepository<>();
        IRepository<AirConditionerRemote> airConditioners = new InMemoryRepository<>();
        IRepository<WirelessMicrophone> microphones = new InMemoryRepository<>();

        ProjectorRemote p1 = new ProjectorRemote("P1", "May chieu 1", "Kho A", 50);
        ProjectorRemote p2 = new ProjectorRemote("P2", "May chieu 2", "Kho A", 90);
        ProjectorRemote p3 = new ProjectorRemote("P3", "May chieu 3", "Kho A", 10);
        for (ProjectorRemote device : List.of(p1, p2, p3)) projectors.add(device);

        AirConditionerRemote a1 = new AirConditionerRemote("A1", "Dieu hoa 1", "Kho B", Set.of("A101"));
        AirConditionerRemote a2 = new AirConditionerRemote("A2", "Dieu hoa 2", "Kho B", Set.of("A203", "A204"));
        airConditioners.add(a1);
        airConditioners.add(a2);

        WirelessMicrophone m1 = new WirelessMicrophone("M1", "Micro 1", "Kho C", 20, 80);
        WirelessMicrophone m2 = new WirelessMicrophone("M2", "Micro 2", "Kho C", 50, 60);
        WirelessMicrophone m3 = new WirelessMicrophone("M3", "Micro 3", "Kho C", 90, 95);
        for (WirelessMicrophone device : List.of(m1, m2, m3)) microphones.add(device);

        check(projectors.getAll().size() == 3 && airConditioners.getAll().size() == 2
                && microphones.getAll().size() == 3, "Them 8 thiet bi vao 3 kho dung chung generic");
        expect(DuplicateIdException.class,
                () -> projectors.add(new ProjectorRemote("P1", "Trung ma", "Kho A", 80)), "Tu choi trung ma");
        expect(NullPointerException.class, () -> projectors.add(null), "Tu choi phan tu null");
        check(projectors.findById("P2") == p2 && projectors.findById("P9") == null, "Tim theo ma");
        check(projectors.findAll(TeachingDevice::isReadyForUse).equals(List.of(p1, p2)), "Loc thiet bi san sang");
        expect(UnsupportedOperationException.class, () -> projectors.getAll().clear(), "Bao ve collection cua kho");

        AllocationService<ProjectorRemote> projectorService = new AllocationService<>(projectors,
                new HighestPriorityStrategy<>(TeachingDevice::getDefaultPriorityScore));
        ProjectorRemote selectedProjector = projectorService.allocate(null);
        check(selectedProjector == p2, "Chon may chieu diem cao nhat: P2");
        check(p2.getStatus() == DeviceStatus.Borrowed, "Thiet bi duoc chon chuyen sang Borrowed");
        expect(DeviceNotReadyException.class, p2::borrow, "Khong muon lai thiet bi dang muon");
        p2.returnDevice();
        check(p2.getStatus() == DeviceStatus.Available, "Tra thiet bi ve Available");
        expect(IllegalStateException.class, p2::returnDevice, "Khong tra thiet bi chua muon");
        expect(DeviceNotReadyException.class, p3::borrow, "Khong muon may chieu pin yeu");

        projectorService.changeStrategy(new FirstAvailableStrategy<>());
        check(projectorService.allocate(null) == p1, "Doi chien luoc: cung ung vien, chon P1 thay vi P2");

        AllocationService<AirConditionerRemote> airService = new AllocationService<>(airConditioners,
                new HighestPriorityStrategy<>(TeachingDevice::getDefaultPriorityScore));
        check(airService.allocate(device -> device.isCompatibleWith("A203")) == a2,
                "Chon dieu hoa A2 tuong thich phong A203");
        expect(NoSuitableItemException.class,
                () -> airService.allocate(device -> device.isCompatibleWith("B999")), "Khong co phong phu hop");

        AllocationService<WirelessMicrophone> micService = new AllocationService<>(microphones,
                new FirstAvailableStrategy<>());
        check(micService.allocate(null) == m2, "Chon micro dau tien san sang: M2");
        check(m2.getDefaultPriorityScore() == 55 && m3.getDefaultPriorityScore() == 92.5,
                "Diem micro la trung binh pin va tin hieu");
        micService.allocate(null);
        expect(NoSuitableItemException.class, () -> micService.allocate(null), "Het micro san sang");
        expect(NoSuitableItemException.class,
                () -> new HighestPriorityStrategy<ProjectorRemote>(TeachingDevice::getDefaultPriorityScore)
                        .select(List.of()), "Chien luoc diem cao nhat tu choi danh sach rong");

        check(!projectors.remove("P9") && projectors.remove("P3") && projectors.findById("P3") == null,
                "Xoa theo ma va xu ly ma khong ton tai");
        expect(IllegalArgumentException.class,
                () -> new ProjectorRemote(" ", "May chieu", "Kho", 50), "Ma khong duoc rong");
        expect(IllegalArgumentException.class,
                () -> new ProjectorRemote("X", "May chieu", "Kho", 101), "Pin khong vuot 100");
        expect(IllegalArgumentException.class,
                () -> new WirelessMicrophone("X", "Micro", "Kho", 50, -1), "Tin hieu khong am");
        check(new ProjectorRemote("X", "May chieu", "Kho", 20).isReadyForUse()
                && new WirelessMicrophone("Y", "Micro", "Kho", 30, 40).isReadyForUse()
                && !new WirelessMicrophone("Z", "Micro", "Kho", 80, 39).isReadyForUse(),
                "Kiem tra nguong pin va tin hieu");

        System.out.println("\nTat ca " + passed + " kiem thu deu thanh cong.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        passed++;
        System.out.println("PASS: " + message);
    }

    private static void expect(Class<? extends Exception> type, Runnable action, String message) {
        try {
            action.run();
        } catch (Exception e) {
            if (!type.isInstance(e)) throw new AssertionError(message, e);
            check(true, message);
            return;
        }
        throw new AssertionError("Khong phat sinh loi: " + message);
    }
}
