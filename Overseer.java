import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
The Overseer is the final boss of the game
It can rush, and shoot in the first phase
In the second phase which it enters at half health, it spawns 4 elite mobs and immediately rushes at the player.
It also gains the bullethell move, and starts doing attacks in quicker succession
 */
public class Overseer extends Enemy
{
    private int maxHp = 4000;  
    private int rushDmg = 50;
    private int maxCooldown = 120;
    private int cooldown = maxCooldown;
    private boolean atWall;
    private boolean rushing;
    private boolean secondPhase = false;
    private boolean dying = false;
    private boolean bulletHell = false;
    private Explosion explosion;
    private int speed = 10;
    private int deathTimer = 300;
    private int width;
    private int height;
    private int shotCooldown = 180;
    private GreenfootSound projSound;
    
    public Overseer(){
        hp = maxHp;
        GreenfootImage image = getImage();
        points = 200;
        image.scale(image.getWidth()*50/100,image.getHeight()*50/100);
        width = getImage().getWidth();
        height = getImage().getHeight();
        
    }
    public void act()
    {
        if (hp< maxHp/2 && secondPhase == false){
            secondPhase = true;
            setImage("SecondStage.png"); 
            GreenfootImage image = getImage();
            image.scale(width, height);
            speed += 4;
            rushing = true;
            maxCooldown = 120;
            //Adding 4 elite mobs around the boss at second phase
            getWorld().addObject(new EliteBrawler(), getX(), getY()+height/2);
            getWorld().addObject(new EliteShooter(), getX(), getY()-height/2);
            getWorld().addObject(new EliteShooter(), getX()-width/2, getY());
            getWorld().addObject(new EliteBrawler(), getX()+width/2, getY());
            GreenfootSound secondPhaseSound = new GreenfootSound("SecondPhase.wav");
            secondPhaseSound.play();
            rushDmg = 70;
        }
        if (dying == true){
            deathTimer--;
            deathAnimation();
            return;
        }
        if (rushing == false && bulletHell == false){
            turnTowards(labWorld.mainRobot.getX(),labWorld.mainRobot.getY());
            if (cooldown > 0){
                    cooldown --;
                }
                
            else{
                int rand = Greenfoot.getRandomNumber(3);
                // Can choose to rush, shoot or bullethell - if second phase
                if (rand == 0){
                    rushing = true;
                }
                else if(rand == 2 && secondPhase){
                    bulletHell = true;
                    shotCooldown = 180;
                }
                else{
                    if (secondPhase){
                        doubleShot();
                    }
                    
                    shootBall();
                }
                cooldown = maxCooldown;
                }
    }
        else if (rushing == true){
            move(speed);
            if (isAtEdge()){
            rushing = false;
            explosion = new Explosion();
            getWorld().addObject(explosion, getX(), getY());
            //To avoid getting stuck into wall
            turnTowards(labWorld.mainRobot.getX(),labWorld.mainRobot.getY());
            move(5);
            
                
            }
            else if(touchingCircle(labWorld.mainRobot)){
                labWorld.mainRobot.damage(rushDmg);
                rushing = false;
                explosion = new Explosion();
                getWorld().addObject(explosion, labWorld.mainRobot.getX(), labWorld.mainRobot.getY());
                hp += 300; // If the boss hits you with a rush, it gains back some health
                
            }
        }
        else if(bulletHell == true){
            if (shotCooldown <0){
                bulletHell = false;
                
            }
            if (shotCooldown %10 == 0){
                randShootBall();
            }
            shotCooldown --;
        }
        
            
        
        healthBar.update(hp);
        
    }
    
    
        
    public void shootBall(){
        projSound = new GreenfootSound("bossProj.wav");
        projSound.play();
        BossProj proj = new BossProj();
        getWorld().addObject(proj, getX(), getY());
        proj.turnTowards(labWorld.mainRobot);
    }
    public void randShootBall(){
        //Shoots a ball at a random angle near the player
        projSound = new GreenfootSound("bossProj.wav");
        projSound.play();
        BossProj proj = new BossProj();
        getWorld().addObject(proj, getX(), getY());
        proj.turnTowards(labWorld.mainRobot);
        proj.turn(Greenfoot.getRandomNumber(90) -45);
    }
    public void doubleShot(){
        //Shoots 2 balls spreading out
        projSound = new GreenfootSound("bossProj.wav");
        projSound.play();
        BossProj proj1 = new BossProj();
        BossProj proj2 = new BossProj();
        getWorld().addObject(proj1, getX(), getY());
        getWorld().addObject(proj2, getX(), getY());
        proj1.turnTowards(labWorld.mainRobot);
        proj2.turnTowards(labWorld.mainRobot);
        proj1.turn(10);
        proj2.turn(-10);
    }
    
    public void addedToWorld (World w){
        healthBar = new SuperStatBar(maxHp,hp,null, 600,20,-120, new Color(255, 0, 255), Color.WHITE, false, Color.BLACK, 1);
        w.addObject(healthBar,600,50);
        labWorld = (LabWorld)getWorld();
    
    }
    
    // 6/12/2026  This method was generated by CodeX, as I asked it to make a circular hitbox for the boss, since for such a big guy, 
    //a square hitbox would be detrimental to the user experience
    
    public boolean touchingCircle(Actor other)
    {
        int radius = height /2; 
        int dx = other.getX() - getX();
        int dy = other.getY() - getY();
    
        return dx * dx + dy * dy <= radius * radius;
    }
    
    public void startDeathAnimation(){
        healthBar.update(hp);
        StartWorld.pauseMusic();
        dying = true;

    }
    public void deathAnimation(){
        if (deathTimer % 10 == 0) {
            // Randomly placed explosions!
            getWorld().addObject(new Explosion(), getX()+ Greenfoot.getRandomNumber(width) - width/2, getY()+Greenfoot.getRandomNumber(height)-height/2);
        }
        if (deathTimer < 0){
            labWorld.addPoints(points); // Adding points here because in the score enemy parent class, shooting the boss after death would keep adding points
            labWorld.gameOver(true);
        }
    }
}
