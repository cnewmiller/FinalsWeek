package edu.depaul.group14;

/**
 * Simple demo application that accepts homework and pretends to work before turning it in
 */
public class FinalsWeek {

    public static class Homework {
        private long num;

        public Homework(final long num) {
            this.num = num;
        }
        public void doHomework() throws InterruptedException {
            Thread.sleep(num);
        }
    }

    public FinalsWeek() {

    }

    public void acceptHomework(Homework homework) {
        try {
            homework.doHomework();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
