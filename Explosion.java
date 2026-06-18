import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
After a second, this explosion is removed from the world
 */
public class Explosion extends Actor
{
    private int maxCountdown = 60;
    private int countdown = maxCountdown;
    private GreenfootSound boom;
    public Explosion(){
        GreenfootImage explode = getImage();
        explode.scale(explode.getWidth()*30/100,explode.getHeight()*30/100);
        boom = new GreenfootSound("explosion.wav");
        boom.setVolume(80);
        boom.play();
    }
    public void act()
    {
        countdown --;
        int transparency = countdown * 255 / maxCountdown;
        getImage().setTransparency(transparency);
        if (countdown == 0){
            getWorld().removeObject(this);
            return;
        }
    }
}
