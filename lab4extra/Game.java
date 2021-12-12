package lab4extra;

import java.util.Random;

public class Game {
    private int cookie;
    private int ans;
    private int count;

    public Game(int cookie) {
        this.cookie = cookie;
        Random r = new Random(cookie);
        this.ans = (r.nextInt(100) + 1);
        this.count = 0;
    }
    
    public String getCookie(){
        return "" + cookie;
    }

    public int getAns(){
        return ans;
    }

    public int getCont(){
        return count;
    }

    public int guess(int guessNumber) {
        count++;
        if(guessNumber == ans) {
            //YES
            return 0;
        } else if(guessNumber < ans) {
            //LOWER
            return 1;
        } else {
            //HIGHER
            return 2;
        }
    }

    public void setNewAns(){
        Random r = new Random(ans);
        this.ans = (r.nextInt(100) + 1);
    }

    private void resetCount() {
        this.count = 0;
    }

    public void update() {
        setNewAns();
        resetCount();
    }
}
