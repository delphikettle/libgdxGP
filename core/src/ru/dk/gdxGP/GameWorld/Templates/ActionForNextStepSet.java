package ru.dk.gdxGP.GameWorld.Templates;
public static class ActionForNextStepSet{
	public static final ActionForNextStep moveAction = new ActionForNextStep() {
		@Override
		public void doSomethingOnStep(Level level) {
			level.Move(16);
		}
	};
}
