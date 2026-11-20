package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;

public class Main {

    public static void main(String[] args) {
        LullabyGame story = new LullabyGame();
        story.game.gameData.setName("Example Game")
                .setSaveFileVersion("v0.0.1")
                .setAuthor("AnotherCastaway")
                .setStartingSceneName("intro")
                .setIconPath("")
                .setShouldDisplayTime(false)
                .setWindowSizeX(800)
                .setWindowSizeY(600);

        story.scene("intro")
                .title("Welcome to a game made in Lullaby!")
                .text("This is a simple example of a game made in Lullaby.")
                .choice("End the game", "ending")
                .choice(
                        Choice.make("See more", "p2")
                                .onSelect(s->System.out.println("You chose to see more in " + s.getCurrentSceneID()))

                );

        story.scene("p2")
                .title("More")
                .text("This is more text.")
                .choice("End the game", "ending");

        story.scene("ending")
                .title("Ending")
                .text("This is the ending of the game.");

        story.start();
    }
}
