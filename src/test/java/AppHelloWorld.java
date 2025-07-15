public class AppHelloWorld {
    public static void main(String[] args) {
        for (int i = 0; i < 250; i++) {
            System.out.println("Hello, World!");
            try {
                Thread.sleep(1000); // Затримка 1000 мс = 1 секунда
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Гарна практика
                System.out.println("Потік перервано");
                break;
            }
        }
    }
}
