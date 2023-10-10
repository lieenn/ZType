import javalib.funworld.*;


//represents a list of words
interface ILoWord {
  
  //Add an IWord to the end of the list
  ILoWord addToEnd(IWord word);
  
  //removing the first letter of a word in the list if the given string matches the first letter
  ILoWord checkAndReduce(String letter);
  
  //filter out IWord with empty strings in the list
  ILoWord filterOutEmpties();
  
  //draw all of the words in list onto given WorldScene
  WorldScene draw(WorldScene scene);
  
  //shifts all words in the list by given coordinates
  ILoWord shift(int changeX, int changeY, int bgWidth);
  
  //does this list has any empty word?
  boolean hasEmpty();
  
  //convert the word to Active if the given string matched the first letter
  ILoWord checkAndConvert(String letter);
  
  //does the given letter matches the first letter of any word in the list?
  public boolean checkLetter(String letter);
  
  //has any of the words in the list reaches the ground?
  boolean isGameOver(int bgHeight);
}

//represents an empty list of words
class MtLoWord implements ILoWord {
  
  /*
   * methods:
   * ... this.sort() ... -- ILoWord
   * ... this.insert(Iword word) ... -- ILoWord
   * ... this.isSorted() ... -- boolean
   * ... this.isSortedHelper ... -- boolean
   * ... this.interleave ... -- ILoWord
   * ... this.merge ... -- ILoWord
   * ... this.comesBefore ... -- boolean
   * ... this.addToEnd ... -- ILoWord
   * ... this.checkAndDeduce ... -- ILoWord
   * ... this.filterOutEmpties ... -- ILoWord
   * ... this.draw ... -- ILoWord
   */
  
  //Add an IWord to the end of the list
  public ILoWord addToEnd(IWord word) {
    return new ConsLoWord(word, this);
  }

  //empty list, nothing to reduce
  public ILoWord checkAndReduce(String letter) {
    return this;
  }

  //empty list, no words to filter out
  public ILoWord filterOutEmpties() {
    return this;
  }

  // empty list, return scene
  public WorldScene draw(WorldScene scene) {
    return scene;
  }

  //shifts all words in the list by given coordinates
  public ILoWord shift(int changeX, int changeY, int bgWidth) {
    return this;
  }

  //empty list doesn't have empty word
  public boolean hasEmpty() {
    return false;
  }

  //return empty list
  public ILoWord checkAndConvert(String letter) {
    return this;
  }
  
  //empty list does not have any word to check
  public boolean checkLetter(String letter) {
    return false;
  }

  //empty words does not have any words on the ground
  public boolean isGameOver(int bgHeight) {
    return false;
  }
}

//represents a non empty list of words
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;
  

  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
    
    /*
     * fields:
     * ... this.first ... -- IWord
     * ... this.rest ... -- ILoWord
     * methods:
     * ... this.sort() ... -- ILoWord
     * ... this.insert(IWord) ... -- ILoWord
     * ... this.isSorted() ... -- boolean
     * ... this.isSortedHelper ... -- boolean
     * ... this.interleave ... -- ILoWord
     * ... this.merge ... -- ILoWord
     * ... this.comesBefore ... -- boolean
     * ... this.addToEnd ... -- ILoWord
     * ... this.checkAndDeduce ... -- ILoWord
     * ... this.filterOutEmpties ... -- ILoWord
     * ... this.draw ... -- ILoWord
     * methods for fields:
     * ... this.first.compareWords(IWord that) ... -- String
     * ... this.first.checkAndReduce(String letter) ... IWord
     * ... this.first.drawWord(WorldScene scene) ... -- WorldScene
     */
  }
  
  //Add an IWord to the end of the list
  public ILoWord addToEnd(IWord word) {
    return new ConsLoWord(this.first, this.rest.addToEnd(word));
  }

  //removing the first letter of a word in the list if the given string matches the first letter
  public ILoWord checkAndReduce(String letter) {
    return new ConsLoWord(first.checkAndReduce(letter), rest.checkAndReduce(letter));
  }
  
  //convert the word to Active if the given string matched the first letter
  public ILoWord checkAndConvert(String letter) {
    if (this.first.checkString(letter)) {
      return new ConsLoWord(this.first.toActive(), this.rest);
    }
    else {
      return new ConsLoWord(this.first, this.rest.checkAndConvert(letter));
    }
  }
  
  //does the given letter matches the first letter of any word in the list?
  public boolean checkLetter(String letter) {
    return this.first.checkString(letter) || this.rest.checkLetter(letter);
  }
  
  //removing the first letter of a word in the list if the given string matches the first letter
  public ILoWord filterOutEmpties() {
    if (this.first.checkEmpty()) {
      return this.rest.filterOutEmpties();
    }
    else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  //draw all of the words in list onto given WorldScene
  public WorldScene draw(WorldScene scene) {
    return this.rest.draw(this.first.drawWord(scene));
  }

  //shifts all words in the list by given coordinates
  public ILoWord shift(int changeX, int changeY, int bgWidth) {
    return new ConsLoWord(this.first.shifts(changeX, changeY, bgWidth), 
        this.rest.shift(changeX, changeY, bgWidth));
  }

  //does this list has any empty word? 
  public boolean hasEmpty() {
    return this.first.checkEmpty() || this.rest.hasEmpty();
  }

  //has any of the words in the list reaches the ground?
  //the game is not over if an empty word reaches the ground
  public boolean isGameOver(int bgHeight) {
    return (this.first.isOnGround(bgHeight) 
        && !this.first.checkEmpty()) 
        || this.rest.isGameOver(bgHeight) ;
  }
}