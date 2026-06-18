import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
Just load the rules background I made in MS Paint
 */
public class RuleWorld extends World
{
    private SuperTextBox returnButton;
    /**
     * Constructor for objects of class RuleWorld.
     * 
     */
    
    public RuleWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 800, 1); 
        returnButton = new SuperTextBox("Back", new Font ("Georgia", false,true,24),100);
        returnButton.toggleCentered();
        addObject(returnButton, 30,20);
    }
    public void act()
    {
        if (Greenfoot.mouseClicked(returnButton)){
            Greenfoot.setWorld(new StartWorld());
    }
    }   
    public void started () {
        StartWorld.continueMusic();
    }
    public void stopped () {
        StartWorld.pauseMusic();
    }
    
}
