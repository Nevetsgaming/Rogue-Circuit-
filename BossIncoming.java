import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
Boss incoming banner made from a pixel art text website
 */
public class BossIncoming extends Actor
{
    /**
     * Act - do whatever the BossIncoming wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int countdown = 200;
    public void act()
    {
        countdown --;
        if (countdown == 0){
            getWorld().removeObject(this);
            return;
        }
    }
}
