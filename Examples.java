import java.awt.Color;
import javalib.funworld.*;
import javalib.worldimages.*;
import tester.Tester;

//all examples and tests for ILoWord
class Examples {
  IWord stormy = new ActiveWord("stormy", 250, 200);
  IWord tormy = new ActiveWord("tormy", 250, 200);
  IWord rainny = new ActiveWord("rainny", 200, 400);
  IWord ainny = new ActiveWord("ainny", 200, 400);
  IWord cloudy = new ActiveWord("Cloudy", 100, 400);
  IWord loudy = new ActiveWord("loudy", 100, 400);
  IWord sunny = new InactiveWord("sunny", 150, 300);
  IWord windy = new InactiveWord("windy", 300, 450);
  IWord snowy = new InactiveWord("Snowy", 200, 500);
  IWord mta = new ActiveWord("", 0, 4);
  IWord foggy = new InactiveWord("foggy", 5, 0);
  IWord no = new InactiveWord("no", 5, 700);
  IWord mtStart = new ActiveWord("", 0, 0);

  ILoWord mt = new MtLoWord();
  ILoWord list1 = new ConsLoWord(this.rainny, this.mt);
  ILoWord list2 = new ConsLoWord(this.stormy, this.list1);
  ILoWord active = new ConsLoWord(this.cloudy, this.list2);

  ILoWord list3 = new ConsLoWord(this.windy, this.mt);
  ILoWord list4 = new ConsLoWord(this.sunny, this.list3);
  ILoWord inactive = new ConsLoWord(this.snowy, this.list4);

  ILoWord list5 = new ConsLoWord(this.cloudy, inactive);
  ILoWord list6 = new ConsLoWord(this.mta, this.list1);
  ILoWord list7 = new ConsLoWord(this.snowy, this.list6);

  ILoWord list8 = new ConsLoWord(this.foggy, this.list2);
  ILoWord list9 = new ConsLoWord(this.mta, this.list2);
  ILoWord list10 = new ConsLoWord(this.no, this.list2);
  ILoWord starting = new ConsLoWord(this.mtStart, this.mt);

  WorldScene scene1 = new WorldScene(20, 20);
  WorldScene scene2 = new WorldScene(50, 50);
  ZTypeWorld world1 = new ZTypeWorld(this.mt);
  ZTypeWorld world2 = new ZTypeWorld(this.list1);
  ZTypeWorld world3 = new ZTypeWorld(this.list9, 4);
  ZTypeWorld world4 = new ZTypeWorld(this.list1, 0, 35);
  ZTypeWorld world5 = new ZTypeWorld(this.list10);
  ZTypeWorld world6 = new ZTypeWorld(this.list1, 2, 35);

  //tests for ILoWord methods
  boolean testAddToEnd(Tester t) {
    return t.checkExpect(this.mt.addToEnd(this.cloudy), 
        new ConsLoWord(this.cloudy, this.mt))
        && t.checkExpect(this.list1.addToEnd(this.sunny), 
            new ConsLoWord(this.rainny, new ConsLoWord(this.sunny, this.mt)))
        && t.checkExpect(this.active.addToEnd(this.windy), 
            new ConsLoWord(this.cloudy, new ConsLoWord(this.stormy, 
                new ConsLoWord(this.rainny, new ConsLoWord(this.windy, this.mt)))))
        && t.checkExpect(this.inactive.addToEnd(this.stormy), 
            new ConsLoWord(this.snowy, new ConsLoWord(this.sunny, 
                new ConsLoWord(this.windy, new ConsLoWord(this.stormy, this.mt)))));
  }

  boolean testcheckAndReduce(Tester t) {
    return t.checkExpect(this.active.checkAndReduce("s"), 
        new ConsLoWord(this.cloudy, new ConsLoWord(this.tormy, this.list1)))
        && t.checkExpect(active.checkAndReduce("c"), 
            new ConsLoWord(this.loudy, new ConsLoWord(this.stormy, this.list1)))
        && t.checkExpect(this.inactive.checkAndReduce("s"), this.inactive)
        && t.checkExpect(this.cloudy.checkAndReduce("c"), this.loudy);
  }

  boolean testfilterOutEmpties(Tester t) {
    return t.checkExpect(this.active.filterOutEmpties(), this.active)
        && t.checkExpect(this.list6.filterOutEmpties(), this.list1)
        && t.checkExpect(new ConsLoWord(this.windy,
            new ConsLoWord(this.mta, this.list1)).filterOutEmpties(), 
            new ConsLoWord(this.windy, this.list1));
  }

  boolean testDraw(Tester t) {
    return t.checkExpect(this.mt.draw(scene1), scene1)
        && t.checkExpect(this.list1.draw(scene1), new WorldScene(20, 20)
            .placeImageXY(new TextImage("rainny", 15, FontStyle.BOLD, Color.PINK), 200, 400))
        && t.checkExpect(this.list3.draw(scene2), new WorldScene(50,50)
            .placeImageXY(new TextImage("windy", 15, FontStyle.BOLD, Color.ORANGE), 300, 450))
        && t.checkExpect(this.list4.draw(scene2), new WorldScene(50,50)
            .placeImageXY(new TextImage("sunny", 15, FontStyle.BOLD, Color.ORANGE), 150, 300)
            .placeImageXY(new TextImage("windy", 15, FontStyle.BOLD, Color.ORANGE), 300, 450))
        && t.checkExpect(this.active.draw(scene1), new WorldScene(20, 20)
            .placeImageXY(new TextImage("Cloudy", 15, FontStyle.BOLD, Color.PINK), 100, 400)
            .placeImageXY(new TextImage("stormy", 15, FontStyle.BOLD, Color.PINK), 250, 200)
            .placeImageXY(new TextImage("rainny", 15, FontStyle.BOLD, Color.PINK), 200, 400));
  }

  boolean testHasEmpty(Tester t) {
    return t.checkExpect(this.mt.hasEmpty(), false)
        && t.checkExpect(this.list5.hasEmpty(), false)
        && t.checkExpect(this.list7.hasEmpty(), true);
  }

  boolean testCheckAndConvert(Tester t) {
    return t.checkExpect(this.inactive.checkAndConvert("w"), 
        new ConsLoWord(this.snowy, 
            new ConsLoWord(this.sunny, 
                new ConsLoWord(new ActiveWord("windy", 300, 450), this.mt))))
        && t.checkExpect(this.inactive.checkAndConvert("s"), 
            new ConsLoWord(new ActiveWord("Snowy", 200, 500), 
                new ConsLoWord(new InactiveWord("sunny", 150, 300), 
                    new ConsLoWord(this.windy, this.mt))));
  }

  boolean testIsGameOver(Tester t) {
    return t.checkExpect(this.list9.isGameOver(400), true)
        && t.checkExpect(this.list8.isGameOver(500), false)
        && t.checkExpect(this.list3.isGameOver(450), true);
  }

  boolean testCheckEmpty(Tester t) {
    return t.checkExpect(this.mta.checkEmpty(), true)
        && t.checkExpect(this.rainny.checkEmpty(), false)
        && t.checkExpect(this.snowy.checkEmpty(), false);
  }

  boolean testCheckString(Tester t) {
    return t.checkExpect(this.rainny.checkString("r"), true)
        && t.checkExpect(this.rainny.checkString("n"), false);
  }

  boolean testCheckLetter(Tester t) {
    return t.checkExpect(this.list1.checkLetter("s"), false);
  }

  //tests for IWord methods
  boolean testcheckAndReduceWord(Tester t) {
    return t.checkExpect(this.stormy.checkAndReduce("s"), this.tormy)
        && t.checkExpect(this.stormy.checkAndReduce("a"), this.stormy);
  }

  boolean testDrawWord(Tester t) {
    return t.checkExpect(this.stormy.drawWord(this.scene1), new WorldScene(20, 20)
        .placeImageXY(new TextImage("stormy", 15, FontStyle.BOLD, Color.RED), 250, 200))
        && t.checkExpect(this.sunny.drawWord(this.scene2), new WorldScene(50, 50)
            .placeImageXY(new TextImage("sunny", 15, FontStyle.BOLD, Color.BLUE), 150, 300));
  }

  boolean testShifts(Tester t) {
    return t.checkExpect(this.stormy.shifts(0, 10, 500), new ActiveWord("stormy", 250, 210))
        && t.checkExpect(this.sunny.shifts(10, 20, 400), new InactiveWord("sunny", 160, 320))
        && t.checkExpect(this.mt.shift(3, 5, 200), this.mt)
        && t.checkExpect(this.list2.shift(30, 10, 400), 
            new ConsLoWord(this.stormy.shifts(30, 10, 400), 
                new ConsLoWord(this.rainny.shifts(30, 10, 400), this.mt)));
  }

  boolean testToActive(Tester t) {
    return t.checkExpect(this.stormy.toActive(), this.stormy)
        && t.checkExpect(this.snowy.toActive(), new ActiveWord("Snowy", 200, 500));
  }


  boolean testIsOnGround(Tester t) {
    return t.checkExpect(this.mta.isOnGround(400), false)
        && t.checkExpect(this.foggy.isOnGround(200), false)
        && t.checkExpect(this.rainny.isOnGround(400), true);
  }


  //Tests for ZTypeWorld methods

  //big bang
  boolean testBigBang(Tester t) {
    ZTypeWorld world = new ZTypeWorld(this.starting);
    int worldWidth = 500;
    int worldHeight = 700;
    double tickRate = 1;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }

  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.world1.makeScene(), this.mt.draw(new WorldScene(500, 700)))
        && t.checkExpect(this.world2.makeScene(), this.list1.draw(new WorldScene(500, 700)));
  }

  boolean testMakeEndScene(Tester t) {
    return t.checkExpect(this.world2.makeEndScene(), 
        new WorldScene(500, 700).placeImageXY(new TextImage("GameOver", 30, FontStyle.BOLD, 
            new Color(157,4,0,255)), 250, 350));
  }

  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(this.world1.onKeyEvent("r"), this.world1)
        && t.checkExpect(this.world3.onKeyEvent("r"), 
            new ZTypeWorld(new ConsLoWord(this.stormy, 
                new ConsLoWord(this.ainny, this.mt)), 4))
        && t.checkExpect(this.world3.onKeyEvent("i"), this.world3);
  }

  boolean testOnTick(Tester t) {
    return t.checkExpect(this.world4.onTick(), 
        new ZTypeWorld(new ConsLoWord(new ActiveWord("rainny", 212, 420), 
            (new ConsLoWord(new InactiveWord("nyvMTd", 94, 0), this.mt))), 4))
        && t.checkExpect(this.world6.onTick(), 
            new ZTypeWorld(new ConsLoWord(new ActiveWord("rainny", 212, 420), this.mt), 1));
  }

  boolean testWordGenerator(Tester t) {
    return t.checkExpect(new ZTypeWorld(this.list1, 0, 35).wordGenerator(10, ""), "EnyvMTdKQk");
  }

  boolean testNumToLetter(Tester t) {
    return t.checkExpect(this.world1.numToLetter(3), 'D')
        && t.checkExpect(this.world1.numToLetter(51), 'z');
  }

  boolean testNewWordOnTop(Tester t) {
    return t.checkExpect(new ZTypeWorld(this.list1, 0, 35).newWordOnTop("hi"), 
        new InactiveWord("hi", 252, 0));
  }
}