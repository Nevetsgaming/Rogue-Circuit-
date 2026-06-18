import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * This is a top down shooter where you are trying to beat the boss
 * Enemies spawn in waves, and you try to defeat them. There is a cap on the amount of elite mobs that can spawn, each wave 
 * to balance the game
 * 
 * The boss is on wave 6, and it has 2 phases. 
 * I added some cool functionality with the boss, so I think it's cool
 * The game is a bit difficult, so if you press "p" you will get infinite health + increased damage, and get to 1 stage before the boss spawns.
 * It might break the enemy tracking however, so just keep that in mind 
 * 
//Music Credits:
// Into the Breach by Ben Prunty
// Full Strength Unleashed by Eclipzodiac

How to play:
Move using WASD, and shoot + aim using mouse click
Collect Power-Ups from defeating enemies



All images were generated using ChatGPT

Credit to circular hitbox code to CodeX
Credit to piercing tracker arraylist to ChatGPT

Sound Credits:
From mixkit
Arcade Game explosion
Fire Explosion
Fuel Explosion
Video game treasure
Cinematic laser gun thunder
Cinematic lasesr swoosh
Short laser gun shot
Bonus Earned in video game

 */
public class LabWorld extends World
{
    public  MainRobot mainRobot;
    
    private SuperStatBar energyBar;
    private  SuperStatBar healthBar;
    
    private Wavecleared banner;
    
    public int score;
    
    private GreenfootSound applied = new GreenfootSound("UpgradeSound.wav");
    
    private Brawler brawl;
    private Shooter shooter;
    private EliteBrawler eliteBrawl;
    private EliteShooter eliteShoot;
    private Overseer boss;
    private int elitesSpawned;
    private int maxElites;
    private int waveNum = 1;
    public  boolean doubleShot = false;
    public  SuperTextBox scoreBox;
    private SuperTextBox waveBox;
    private BossIncoming bossBanner;
    private int actNum = 0;
    private boolean waveCleared = true;
    private boolean bossSpawned = false;
    private boolean freezeRobots = false;
    private int enemiesToSpawn;
    public int enemiesKilled;
    private int enemyTrack;
    private int side;
    private int type;
    private int waveCooldown = 0;
    private int d = 20;
    private int timeSinceSpawn = 0;
 
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public LabWorld()
    {    
        
        super(1200, 800, 1); 
        
        mainRobot = new MainRobot();
        addObject(mainRobot, getWidth() / 2, getHeight()/2);
        energyBar = new SuperStatBar (mainRobot.maxE(), mainRobot.getEnergy(),null, 800,20,-100, Color.CYAN, Color.WHITE, false, Color.BLACK, 1);
        healthBar = new SuperStatBar(mainRobot.maxH(),mainRobot.getHealth(),null, 800,20,-100, Color.RED, Color.WHITE, false, Color.BLACK, 1);
        bossBanner = new BossIncoming();
        addObject(energyBar, 600, getHeight() - 35);
        addObject(healthBar, 600, getHeight() - 65);
        score = 0;
        
        scoreBox = new SuperTextBox("Score: "+score, new Font ("Calibri", false,true,15),125);
        scoreBox.toggleCentered();
        waveBox = new SuperTextBox("Waves Cleared: "+(waveNum-1), new Font ("Calibri", false,true,15),125);
        waveBox.toggleCentered();
        addObject(scoreBox, 100, getHeight()-50);
        addObject(waveBox, 100, getHeight()-25);
        setPaintOrder(SuperStatBar.class,SuperTextBox.class, MainRobot.class);
    }
    
    public void act(){
        checkKeys();
        energyBar.update(mainRobot.getEnergy());
        healthBar.update(mainRobot.getHealth());
        scoreBox.update("Score: "+score);
        actNum++;
        if (actNum%60 == 0 && mainRobot.getEnergy() <= mainRobot.maxE()){
                mainRobot.changeEnergy(mainRobot.regenRate());
            }
        
    // add spawning of mobs based on wave numbers 
        //Hard-Coded waves
        if (waveCleared == true && waveCooldown <=0){
            if (waveNum < 6){
                enemiesToSpawn = 2+2*waveNum;
                enemyTrack = enemiesToSpawn;
                elitesSpawned = 0;
                maxElites = waveNum;
      
            }

            if (waveNum == 6){
                //spawn the boss 
                boss = new Overseer();
                
                addObject(boss, 1100, 400);
                bossSpawned = true;
                
            }
            waveCleared = false;
          
        }
        
        if (waveCooldown > 0){
            waveCooldown --;
        }
        else if(bossSpawned == true){
            // If the boss has been spawned, then only do a trickle amount of enemies
            side = Greenfoot.getRandomNumber(4);
            type = Greenfoot.getRandomNumber(2);
            if (Greenfoot.getRandomNumber(240) == 0){
                spawn();
        }
        }
        else if((enemiesToSpawn >0 && Greenfoot.getRandomNumber(200) == 0) || (actNum - timeSinceSpawn  >240 && enemiesToSpawn > 0)){
            spawn();
            //guarenteed to spawn at least one mob per cycle in a wave, and has a chance to spawn multiple
            //Due to the random nature, it could spawn many mobs at once, and totally overwhelm the player
            for (int i = 0; i <Greenfoot.getRandomNumber(waveNum/2 +1); i++){
                if (enemiesToSpawn > 0){
                    spawn();
            }
            }
            timeSinceSpawn = actNum;
        }
        if (bossSpawned == false && enemiesKilled == enemyTrack){
            waveCleared = true;
            waveNum ++;
            waveCooldown = 240;
            waveBox.update("Waves Cleared: "+(waveNum-1));
            mainRobot.changeEnergy(mainRobot.maxE()/3); // give 1/3 of the energy back each wave
            banner = new Wavecleared();
            if (waveNum == 6){
                addObject(bossBanner, getWidth()/2, getHeight()/2);
                StartWorld.bossMusic();
            }
            else
            {addObject(banner, getWidth()/2, getHeight()/2);
            }
            enemiesKilled = 0;
            
        }
       
        
        
    }
    
    
    private void checkKeys(){
        if (mainRobot.getEnergy() > 0){
            if (Greenfoot.isKeyDown("a")){
                mainRobot.setLocation(mainRobot.getX() - mainRobot.getSpeed(),mainRobot.getY());
            }
            if (Greenfoot.isKeyDown("d")){
                mainRobot.setLocation(mainRobot.getX() + mainRobot.getSpeed(),mainRobot.getY());
            }
            if (Greenfoot.isKeyDown("w")){
                mainRobot.setLocation(mainRobot.getX(),mainRobot.getY() - mainRobot.getSpeed());
            }
            if (Greenfoot.isKeyDown("s")){
                mainRobot.setLocation(mainRobot.getX(),mainRobot.getY() + mainRobot.getSpeed());
            }
            
            
        }
        if (Greenfoot.isKeyDown("p")){
            mainRobot.increaseMaxE(10000);
            mainRobot.changeEnergy(10000);
            mainRobot.increaseMaxH(10000);
            mainRobot.heal(10000);
            mainRobot.increaseWeapon();
            mainRobot.increaseWeapon();
            mainRobot.cheatDamage();
            applied.play();
            waveNum = 5;
            
        }
        
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null && Greenfoot.mouseClicked(null)){
            
            //So you can shoot at a faster rate at energy = 0, but the tradeoff is that you can't move.
            
            if (mainRobot.getEnergy() >0){
                
                shoot(m.getX(), m.getY());
            }
        }
    }
    public void spawn(){
        side = Greenfoot.getRandomNumber(4);
        type = Greenfoot.getRandomNumber(2);
        //Choose a side and a type
        //Depending on the side, it spawns on the border of that wall
        if (side == 0){
            spawnEnemy(type, 0,Greenfoot.getRandomNumber(getHeight()));
            }
        if (side == 1){
            spawnEnemy(type, getWidth(),Greenfoot.getRandomNumber(getHeight()));
            }
        if (side == 2){
            spawnEnemy(type, Greenfoot.getRandomNumber(getWidth()),0);
            }
        if (side == 3){
            spawnEnemy(type, Greenfoot.getRandomNumber(getWidth()), getHeight());
            }
        enemiesToSpawn --;
    }
    public void shoot(int mx, int my){
        
        mainRobot.shot();
        
        
        if (doubleShot){
            //Finds the x and y components of the perpendicular vector to where the mouse is pointing from mainrobot
            //Then adds this to where the laser spawns
            double angle = Math.atan2(my - mainRobot.getY(),mx - mainRobot.getX());
            double px = Math.sin(angle);
            double py =  -Math.cos(angle);
            int leftX  = (int)(mainRobot.getX() + px * d);
            int leftY  = (int)(mainRobot.getY() + py * d);

            int rightX = (int)(mainRobot.getX() - px * d);
            int rightY = (int)(mainRobot.getY() - py * d);
            BLaser left = new BLaser();
            BLaser right = new BLaser();

            addObject(left, leftX, leftY);
            addObject(right, rightX, rightY);
            left.turnTowards((int)(mx + px*d), (int)(my+ py*d));
            right.turnTowards((int)(mx - px*d), (int)(my - py*d));
        }
        else{
            BLaser laser = new BLaser();
            addObject(laser, mainRobot.getX(),mainRobot.getY());
            laser.turnTowards(mx, my);
        }
        
    }
    
   
    public  void addPoints(int points){
        score += points;
    }
    public  void gameOver(boolean victory){
        Greenfoot.setWorld(new EndWorld(victory, score));
    }
    public void spawnEnemy(int type, int x, int y){
        if (type == 0){
            //Chance to spawn an elite mob, but the number that can spawned is limited
            if (Greenfoot.getRandomNumber(10-waveNum) == 0 && (elitesSpawned < maxElites || bossSpawned == true)){
                eliteBrawl = new EliteBrawler();
                addObject(eliteBrawl,x,y);
                elitesSpawned++;
            }
            else
            {brawl = new Brawler();
            addObject(brawl, x,y);
        }
        }
        else{
            if (Greenfoot.getRandomNumber(10-waveNum) == 0 && (elitesSpawned < maxElites || bossSpawned == true)){
                eliteShoot = new EliteShooter();
                addObject(eliteShoot,x,y);
                elitesSpawned ++;
            }
            else{
            shooter = new Shooter();
            addObject(shooter, x,y);}
        }
        
    }
    public void addKills(){
        enemiesKilled ++;
    }
    public  boolean enemiesFrozen()
    {
        return freezeRobots;
    }
    public  void freezeEnemies(){
        //If the boss is dead, this will be true and the robots stop trying to shoot you
        freezeRobots = true;
    }
    public void started () {
        StartWorld.continueMusic();
    }
    public void stopped () {
        StartWorld.pauseMusic();
    }
    public  void updateBars (){
        //updates the max val for the stat bars
        healthBar.setMaxVal(mainRobot.maxH());
        energyBar.setMaxVal(mainRobot.maxE());
    }
 
}
