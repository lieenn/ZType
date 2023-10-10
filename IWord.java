import java.awt.Color;
import javalib.funworld.*;
import javalib.worldimages.*;

//represents a word in the ZType game
interface IWord {
  
  //removing the first letter of a word if the given string matches the first letter
  IWord checkAndReduce(String letter);
  
  //check if the word is empty
  boolean checkEmpty();
  
  //draw a word on a given scene
  WorldScene drawWord(WorldScene scene);
  
  //shifts the word down
  IWord shifts(int changeX, int changeY, int bgWidth);

  //turns the word active
  IWord toActive();

  //check if the given letter matches the first letter of a word
  boolean checkString(String letter);
  
  //has the word touches the ground?
  public boolean isOnGround(int bgHeight);
}

abstract class AWord implements IWord {
  String word;
  int x;
  int y;
  
  AWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }
  
  //removing the first letter of this word if the given string matches the first letter
  abstract public IWord checkAndReduce(String letter);
  
  //check if the word is empty
  public boolean checkEmpty() {
    return this.word.equals("");
  }
  
  //draw a word on a given scene
  abstract public WorldScene drawWord(WorldScene scene);
  
  //shifts the word down and left or right depends on the word's position
  abstract public IWord shifts(int changeX, int changeY, int bgWidth);
  
  //turns the word to active word
  abstract public IWord toActive();
  
  //does the given letter matches the first letter of a word? (not case sensitive)
  public boolean checkString(String letter) {
    return this.word.toLowerCase().startsWith(letter.toLowerCase());
  }
  
  //has the word touches the ground?
  public boolean isOnGround(int bgHeight) {
    return this.y == bgHeight;
  }
}

//represents an active word in the ZType game
class ActiveWord extends AWord {

  ActiveWord(String word, int x, int y) {
    super(word, x, y);
    
    /*
     * fields:
     * ... this.word ... -- String
     * ... this.x ... -- int
     * ... this.y ... -- int
     * methods:
     * ... this.compareWords(IWord) ... -- int
     * ... this.helper(String) -- int
     * ... this.checkAndReduce(String)
     * ... this.checkEmpty() ... -- boolean
     * ... this.drawWord(WorldScene) ... -- WorldScene
     * ... this.shifts(int, int) ... -- IWord
     * methods for fields: none
     */
  }

  //removing the first letter of this word if the given string matches the first letter
  public IWord checkAndReduce(String letter) {
    if (this.checkString(letter)) {
      return new ActiveWord(this.word.substring(1), this.x, this.y);
    }
    else {
      return this;
    }
  }
  
  //draw a word on a given scene
  public WorldScene drawWord(WorldScene scene) {
    return scene
      .placeImageXY(new TextImage(this.word, 15, FontStyle.BOLD, new Color(57,172,203,255)), 
        this.x, this.y);
  }

  //shifts the word down and left or right depends on the word's position
  public IWord shifts(int changeX, int changeY, int bgWidth) {
    int bgMiddle = bgWidth / 2;
    if (this.x < bgMiddle) {
      return new ActiveWord(this.word, this.x + changeX, this.y + changeY);
    }
    else if (this.x > bgMiddle) {
      return new ActiveWord(this.word, this.x - changeX, this.y + changeY);
    }
    else {
      return new ActiveWord(this.word, this.x, this.y + changeY);
    }
  }

  //turns the word to active word
  public IWord toActive() {
    return this;
  }
}

//represents an inactive word in the ZType game
class InactiveWord extends AWord {

  InactiveWord(String word, int x, int y) {
    super(word, x, y);
    
    /*
     * fields:
     * ... this.word ... -- String
     * ... this.x ... -- int
     * ... this.y ... -- int
     * methods:
     * ... this.compareWords(IWord that) ... -- int
     * ... helper(String word) -- int
     * ... checkAndReduce(String letter)
     * ... checkEmpty() ... -- boolean
     * ... drawWord(WorldScene scene) ... -- WorldScene
     * ... this.shifts(int, int) ... -- IWord
     * methods for fields: none
     */
    
  }
  
  // checkAndReduce does not apply to inactive words
  public IWord checkAndReduce(String letter) {
    return this;
  } 
  
  //draw a word on a given scene
  public WorldScene drawWord(WorldScene scene) {
    return scene
      .placeImageXY(new TextImage(this.word, 15, FontStyle.BOLD, new Color(225,137,180,255)), 
        this.x, this.y);
  }

  //shifts the word down
  public IWord shifts(int changeX, int changeY, int bgWidth) {
    int bgMiddle = bgWidth / 2;
    if (this.x < bgMiddle) {
      return new InactiveWord(this.word, this.x + changeX, this.y + changeY);
    }
    else if (this.x > bgMiddle) {
      return new InactiveWord(this.word, this.x - changeX, this.y + changeY);
    }
    else {
      return new InactiveWord(this.word, this.x, this.y + changeY);
    }
  }

  //turns the word active word
  public IWord toActive() {
    return new ActiveWord(this.word, this.x, this.y);
  }
}