package ru.dk.gdxGP;

import java.util.ArrayList;

public class Particle extends Component
{
	private ArrayList<Component> components;
	Particle(float x, float y,float m){
		super(x,y,m);
		components=new ArrayList<Component>(1);
	}
	Particle(Component component){
		super(component.getX(),component.getY(),component.getM());
		components=new ArrayList<Component>(1);
		components.add(component);
		component.setOwner(this);
	}
}
