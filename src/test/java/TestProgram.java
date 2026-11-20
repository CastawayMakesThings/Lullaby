import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;

public class TestProgram {
    public static void main(String[] args) {
        LullabyGame engine = new LullabyGame();
        engine.gameState.addStat("testStat", "Level", 10);

        engine.scene("start")
                        .text("Welcome to the game!");

        engine.scene("next")
                .text("You are now in the next scene!");

        engine.goToScene("start");
        engine.start();
    }
}
