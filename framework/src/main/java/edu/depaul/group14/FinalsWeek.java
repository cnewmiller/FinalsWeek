package edu.depaul.group14;

/**
 * Simple demo application that accepts homework and pretends to work before turning it in
 */
public class FinalsWeek {

    public static class Homework {
        private final long num;

        public Homework(final long num) {
            this.num = num;
        }
        public long doHomework() throws InterruptedException {
            Thread.sleep(num);
            return num;
        }
        public long getNum() {
            return num;
        }
    }

    public FinalsWeek() {

    }

    public long acceptHomework(Homework homework) {
        try {
            return homework.doHomework();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1L;
    }
}
