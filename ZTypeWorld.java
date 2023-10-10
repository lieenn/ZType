import javalib.funworld.*;
import java.util.Random;
import javalib.worldimages.*;
import java.awt.Color;


//represents a World program 
class ZTypeWorld extends World {
  ILoWord wordList;
  Random random;
  int counter;

  //Constants
  static int BG_WIDTH = 500;
  static int BG_HEIGHT = 700;

  //constructor that takes in a ILoWord and a tick counter
  ZTypeWorld(ILoWord wordList, int counter) {
    this.wordList = wordList;
    this.counter = counter;
    this.random = new Random();
  }

  //constructor that takes in a ILoWord
  ZTypeWorld(ILoWord wordList) {
    this.wordList = wordList;
    this.random = new Random();
    this.counter = 0;
  }

  ZTypeWorld(ILoWord wordList, int counter, int seed) {
    this.wordList = wordList;
    this.counter = counter;
    this.random = new Random(seed);
  }

  //renders the state of ZTypeWorld
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(BG_WIDTH, BG_HEIGHT);
    return this.wordList.draw(ws);
  }

  //draw the ending scene
  public WorldScene makeEndScene() {
    WorldScene ws = new WorldScene(BG_WIDTH, BG_HEIGHT);
    return ws.placeImageXY(new TextImage("GameOver", 30, FontStyle.BOLD, new Color(157,4,0,255)),
        BG_WIDTH / 2, BG_HEIGHT / 2);
  }

  //handles key events for this TaskWorld
  public World onKeyEvent(String key) {
    if (this.wordList.hasEmpty() & this.wordList.checkLetter(key)) {
      return new ZTypeWorld(this.wordList.filterOutEmpties()
          .checkAndConvert(key).checkAndReduce(key), this.counter);
    }
    else {
      return new ZTypeWorld(this.wordList.checkAndReduce(key), this.counter);
    }
  }

  //shifts word down by 20 and left or right depends on current location
  //spawns a new word with random letters every 5 ticks
  public World onTick() {
    if (this.counter == 0) {
      return new ZTypeWorld(this.wordList.shift(random.nextInt(20), 20, BG_WIDTH)
          .addToEnd(newWordOnTop(wordGenerator(6, ""))), 4);
    }
    else {
      return new ZTypeWorld(this.wordList.shift(random.nextInt(20), 20, BG_WIDTH), 
          this.counter - 1);
    }
  }

  //the world ends when one of the IWords in the list has reached the ground
  public WorldEnd worldEnds() {
    if (this.wordList.isGameOver(BG_HEIGHT)) {
      return new WorldEnd(true, this.makeEndScene());
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  //generates a given number of letters string with random words
  public String wordGenerator(int lettersToGen, String currentWord) {
    if (lettersToGen == 0) {
      return currentWord;
    }
    else {
      return wordGenerator(lettersToGen - 1, 
          currentWord + numToLetter(this.random.nextInt(52)));
    }
  }

  //pick a random letter from the alphabet string
  public char numToLetter(int num) {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    return alphabet.charAt(num);
  }

  //turns the given word to an inactive word on top of the screen
  public IWord newWordOnTop(String word) {
    return new InactiveWord(word, this.random.nextInt(BG_WIDTH), 0);
  }
}
