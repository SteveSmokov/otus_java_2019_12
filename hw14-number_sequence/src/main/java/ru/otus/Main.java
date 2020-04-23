package ru.otus;

public class Main {
    private static final int START_VALUE = 1;
    private static final int MAX_VALUE = 10;
    private static final int ITERATIONS_COUNT = 1;
    private static final int TIME_OUT = 100;
    private final Flag flag = new Flag();

    public static void main(String[] args) throws InterruptedException {
        new Main().startThreads(ITERATIONS_COUNT);
    }

    public void startThreads(int iterationsCount) throws InterruptedException {
        for (int i = 1; i <= iterationsCount; i++){
            System.out.println(" -----------------------");
            System.out.printf("%-6s %-16s %s\n","|", "Iteration #" + i,"|");
            System.out.printf("%s %-9s %s %-9s %s\n","|","Thread 1","|","Thread 2","|");
            Thread thread1 = new Thread(this::start);
            Thread thread2 = new Thread(this::start);
            thread1.setName("1");
            thread2.setName("2");
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            System.out.println(" -----------------------");
        }
    }

    private void start() {
        for (int i = START_VALUE; i < MAX_VALUE; i++){
            print(i);
        }
        for (int i = MAX_VALUE; i >= START_VALUE; i--){
            print(i);
        }
    }

    private void print(int i) {
        synchronized (flag){
            try {
                Thread.sleep(TIME_OUT);
                while ((Thread.currentThread().getName().equals("1") && flag.getValue()) ||
                        (Thread.currentThread().getName().equals("2") && !flag.getValue())){
                    flag.wait();
                }
                System.out.printf("%-5s %-5s %-5s %-5s %s\n","|",
                        flag.getValue()?String.valueOf(i):"","|",
                        flag.getValue()?"":String.valueOf(i),"|");
                flag.setValue(!flag.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag.notify();
        }
    }

    private class Flag{
        private boolean value = true;

        public boolean getValue() {
            return this.value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }
}
