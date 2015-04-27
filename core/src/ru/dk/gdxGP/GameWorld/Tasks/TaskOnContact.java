package ru.dk.gdxGP.GameWorld.Tasks;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import ru.dk.gdxGP.GameWorld.Interfaces.LevelElement;
import ru.dk.gdxGP.GameWorld.Level;
import ru.dk.gdxGP.GameWorld.Task;

public class TaskOnContact extends Task {
    private final LevelElement element1;
    private final LevelElement element2;
    private boolean contacted=false;

    public TaskOnContact(final LevelElement element1, final LevelElement element2) {
        this.element1 = element1;
        this.element2 = element2;
        if (element1.getLevel()!=element2.getLevel())
            return;
        Level level=element1.getLevel();
        level.multiListener.addContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                System.out.println("contacted");
                if((contact.getFixtureA().getBody().getUserData()==element1&&contact.getFixtureB().getBody().getUserData()==element2)
                        ||(contact.getFixtureA().getBody().getUserData()==element2&&contact.getFixtureB().getBody().getUserData()==element1)){
                    contacted=true;
                    System.out.println("contacted with");
                }
            }
            @Override
            public void endContact(Contact contact) {
                if((contact.getFixtureA().getUserData()==element1&&contact.getFixtureB().getUserData()==element2)
                        ||(contact.getFixtureA().getUserData()==element2&&contact.getFixtureB().getUserData()==element1)){
                    contacted=false;
                }
            }
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });

    }

    @Override
    protected boolean check() {
        return contacted;
    }
}
