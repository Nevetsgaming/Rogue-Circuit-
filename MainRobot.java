import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
/**
 * Write a description of class MainRobot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainRobot extends SuperSmoothMover
{
    public GreenfootImage mainBot, blueLaser;
    
    private GreenfootSound[] shootSounds;
    private GreenfootSound deathSound;
    private int shootSoundIndex;
    //The SuperStatBars
    
    
    //Player stats
    private int maxEnergy = 1000;
    private int energy = 1000;
    private static int weaponStage = 0;
    private int maxHealth = 150;
    private int health = maxHealth;
    private int damage = 10;
    private double speed = 3.0;
    private int myActNumber;
    private int shotCost = 50;
    private int waveNum;
    private int enemiesToSpawn;
    private int delaySpawner;
    
  
    public MainRobot ()
    {
        GreenfootImage image = getImage();
        image.scale(image.getWidth()*20/100,image.getHeight()*20/100);       
        shootSoundIndex = 0;
        shootSounds = new GreenfootSound[20];
        for (int i = 0; i < shootSounds.length; i++){
            shootSounds[i] = new GreenfootSound("mainshot.wav");
            shootSounds[i].setVolume(90);
        }
        deathSound = new GreenfootSound("weExplode.wav");
        weaponStage = 0;
    }
    
    public static void init(){
      
    }
    public void act()
    {
        
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null){
            turnTowards(m.getX(), m.getY());
        }
        
    
    }
    public double getSpeed(){
        return speed;
    }
    public int getEnergy(){
        return energy;
    }
    public int maxE(){
        return maxEnergy;
    }
    public void increaseMaxE(int increase){
        maxEnergy += increase;
        labWorld.updateBars();
    }
    public int maxH(){
        return maxHealth;
    }
    public void increaseMaxH(int increase){
        maxHealth += increase;
        labWorld.updateBars();
    }
    public int getHealth(){
        return health;
    }
    
    public void shot(){
        shootSounds[shootSoundIndex].play();
        shootSoundIndex++;
        // Wrap back to the beginning when we reach the end of the array.
        if (shootSoundIndex == shootSounds.length){
            shootSoundIndex = 0;
        }
        energy = Math.max(0,energy -shotCost);
    }
    public void damage(int dmg){
        health = Math.max(0, health - dmg);
        
        if (health == 0){
            deathSound.play();
            labWorld.gameOver(false);
        }
        
    }
    public void heal(int heal){
        damage(-heal);
        health = Math.min(health,maxHealth);
    }
    public int getDamage(){
        return damage;
    }
    public int ePerShot(){
        return shotCost;
    }
    public void changeEnergy(int diff){
        energy = Math.min(maxEnergy,energy + diff);
    }
    public void increaseWeapon(){
        weaponStage++;
        if (weaponStage  ==1 ){
            labWorld.doubleShot = true;
        }
        if (weaponStage == 2){
            BLaser.upgraded = true;
        }
        else{
            damage ++;
        }
        
    }
    public void cheatDamage(){
        damage += 20;
    }
    public int regenRate(){
        //Energy regen rate
        return (75);
    }
    public static int getWeaponStage(){
        return weaponStage;
    }
    
}
