package Lessons;

public class ABCWriter {
    private static final Object monitor = new Object();
    private static volatile char startLetter = 'A';

    static class WaitNotifyClass implements Runnable {
        private char currentLetter;
        private char nextLetter;

        public WaitNotifyClass(char currentLetter, char nextLetter) {
            this.currentLetter = currentLetter;
            this.nextLetter = nextLetter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                synchronized (monitor) {
                    try {
                        while (startLetter != currentLetter)
                            monitor.wait();
                        System.out.print(startLetter);
                        startLetter = nextLetter;
                        monitor.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public static void main(String[] args) {
            new Thread(new WaitNotifyClass('A', 'B')).start();
            new Thread(new WaitNotifyClass('B', 'C')).start();
            new Thread(new WaitNotifyClass('C', 'A')).start();

        }


    }
}
