/**
 * ゲームの進行自体を取り仕切るクラス
 * ・キーボード入力
 * ・ゲーム内の各オブジェクトの管理

 */

import java.awt.*;
import java.awt.event.*;

public class GameMaster extends Canvas implements KeyListener {

  // 11 ■ フィールド変数
  Image buf; // 仮の画面としての buffer に使うオブジェクト(Image クラス)
  Graphics buf_gc; // buffer のグラフィックスコンテキスト (gc)用オブジェクト
  Dimension d; // アプレットの大きさを管理するオブジェクト
  private int imgW, imgH; // キャンバスの大きさ

  private int enmyAnum = 20; // 敵Aの数
  private int enmyBnum = 20; // 敵Bの数
  private int ftrBltNum = 10; // 自機の弾の数
  private int enmyABltNum = 10; //敵Aの弾の数
  private int mode = -1; // -1: タイトル画面, -2: ゲームオーバー,　1~ ゲームステージ
  private int i, j;

  Fighter ftr; // 自機
  FighterBullet ftrBlt[] = new FighterBullet[ftrBltNum]; // 自機の弾

  EnemyA enmyA[] = new EnemyA[enmyAnum]; // 敵機A
  EnemyB enmyB[] = new EnemyB[enmyBnum]; // 敵機B
  Boss boss; //ボス
  BackGroundImage backgroundimage; //背景


  // ■ コンストラクタ
  /**
   * ゲームの初期設定
   * ・描画領域(Image)とGC(Graphics)の作成
   * ・敵,自機,弾オブジェクトの作成
   */
  GameMaster(int imgW, int imgH) { // コンストラクタ
    
    // 描画領域のサイズ
    this.imgW = imgW; 
    this.imgH = imgH; 

    setSize(imgW, imgH); //描画領域のサイズを設定

    addKeyListener(this);
    

    ftr = new Fighter(imgW, imgH); // 自機のオブジェクトを実際に作成

    for (i = 0; i < ftrBltNum; i++) { // 自機弾のオブジェクトを実際に作成
      ftrBlt[i] = new FighterBullet();
    }

    for (i = 0; i < enmyAnum; i++) { // 敵Aのオブジェクトを実際に作成
      enmyA[i] = new EnemyA(imgW, imgH);
    }

    for (i = 0; i < enmyBnum; i++) { // 敵Bのオブジェクトを実際に作成
      enmyB[i] = new EnemyB(imgW, imgH);
    }

    boss = new Boss(imgW, imgH); //ボスのオブジェクトを実際に作成

    backgroundimage = new BackGroundImage(); //背景のオブジェクトを実際に作成

  }

  // ■ メソッド
  // コンストラクタ内で createImage を行うと peer の関連で
  // nullpointer exception が返ってくる問題を回避するために必要

  public void addNotify() {
    super.addNotify();
    buf = createImage(imgW, imgH); // buffer を画面と同サイズで作成
    buf_gc = buf.getGraphics();
  }

  // ■ メソッド (Canvas)
  public void paint(Graphics g) {

    //背景
    buf_gc.drawImage(backgroundimage.start, 0, 0, imgW, imgH,this); //指定された矩形の内部に収まるようにスケーリングして、指定されたイメージの利用可能な部分を描きます。

    //buf_gc.setColor(Color.white); // gcの色を白に
    //buf_gc.fillRect(0, 0, imgW, imgH); // gc を使って白の四角を描く(背景の初期化)

    switch (mode) {
      case -2: // ゲームオーバー画面(スペースキーを押されたらタイトル画面
        buf_gc.setColor(Color.black); // ゲームオーバー画面を描く
        buf_gc.drawString(" == Game over == ", imgW / 2 - 80, imgH / 2 - 20);
        buf_gc.drawString(" Hit SPACE key  ", imgW / 2 - 80, imgH / 2 + 20);
        ftr.score = 0;
        break;
      case -1: // タイトル画面(スペースキーを押されたらゲーム開始)
        buf_gc.setColor(Color.black); // タイトル画面を描く
        buf_gc.drawString(
          " == Shooting Game Title == ",
          imgW / 2 - 80,
          imgH / 2 - 20
        );
        buf_gc.drawString(
          "Hit SPACE bar to start game",
          imgW / 2 - 80,
          imgH / 2 + 20
        );
        ftr.score = 0;
        break;
      case 1: //ボスバトル警告画面

      //敵を全部無効化する
        for (i = 0; i < enmyAnum; i++) {
          enmyA[i].hp = 0;
        }
        for (i = 0; i < enmyBnum; i++) {
          enmyB[i].hp = 0;
        }

        buf_gc.setColor(Color.red);
        buf_gc.drawString("Boss Battle Will Begin!", imgW / 2, imgH / 2);
        boss.hp = 50;
        break;
        
      case 2: //ボスバトル

      buf_gc.drawImage(backgroundimage.bossbattle, 0, 0, imgW, imgH,this); //指定された矩形の内部に収まるようにスケーリングして、指定されたイメージの利用可能な部分を描きます。


        //自機の体力、現在のスコアを表示
        buf_gc.setColor(Color.red);
        String hp_string_Boss = String.valueOf(ftr.hp);
        String score_string_Boss = String.valueOf(ftr.score);
        
        buf_gc.drawString("Fighter_hp:" + hp_string_Boss, imgW * 1 / 2, imgH - 10);
        buf_gc.drawString("score:" + score_string_Boss,imgW * 4 / 5, 10);


        // *** 自分の弾を発射 ***
        if (ftr.sflag == true && ftr.delaytime == 0) { // もしスペースキーが押されていて&待ち時間がゼロ
          for (i = 0; i < ftrBltNum; i++) { // 全部の弾に関して前から探査して
            if (ftrBlt[i].hp <= 0) { // 非アクティブの (死んでいる)弾があれば
              ftrBlt[i].revive(ftr.x, ftr.y); // 自機から弾を発射して
              ftr.delaytime = 5; //自機の弾発射待ち時間を元に戻して
              break; // for loop を抜ける
            }
          }
        } else if (ftr.delaytime > 0) { // 弾を発射しない(出来ない)場合は
          ftr.delaytime--; // 待ち時間を1減らす
        }

        if (boss.hp > 0) { // ボスが生きていたら
          ftr.collisionCheck(boss); // 自機と衝突チェック
        }
        for (i = 0; i < ftrBltNum; i++) { // 全ての自弾に関して
          if (ftrBlt[i].hp > 0) { // 自弾が生きていたら
            ftrBlt[i].collisionCheck(boss); // 自弾との衝突チェック
          }
        }
      
        if (ftr.hp < 1) { // ゲーム終了
          mode = -2;
        }
        if (boss.hp <= 0) {
          mode = 3;
        }
        for (i = 0; i < ftrBltNum; i++) {
          ftrBlt[i].move(buf_gc, imgW, imgH);
        }

        boss.move(buf_gc, imgW, imgH);

        ftr.move(buf_gc, imgW, imgH);

        break;
      case 3: //終了画面

        buf_gc.setColor(Color.red); // タイトル画面を描く
        buf_gc.drawString(" == YOU WIN == ", imgW / 2 - 80, imgH / 2 + 80);
        buf_gc.drawString("Continue?",imgW / 2 - 80, imgH / 2 + 20);
        ftr.score = 0;
        break;

      default: // 通常ステージ

      //背景の描画
      buf_gc.drawImage(backgroundimage.mainstage, 0, 0, imgW, imgH,this); //指定された矩形の内部に収まるようにスケーリングして、指定されたイメージの利用可能な部分を描きます。

        //自機の体力、点数の描画
        buf_gc.setColor(Color.red);
        String hp_string = String.valueOf(ftr.hp);
        String score_string = String.valueOf(ftr.score);
        
        buf_gc.drawString("Fighter_hp:" + hp_string, imgW * 1 / 2, imgH - 10);
        buf_gc.drawString("score:" + score_string,imgW * 4 / 5, 10);


        // *** ランダムに敵を生成 ***
        makeEnmy:if (Math.random() < 0.1) { // 10%の確率で一匹生成
          for (i = 0; i < enmyAnum; i++) {
            if (enmyA[i].hp <= 0) {
              enmyA[i].revive(imgW, imgH);
              ftr.score++;
              break;
            }
          }
          for (i = 0; i < enmyBnum; i++) {
            if (enmyB[i].hp <= 0) {
              enmyB[i].revive(imgW, imgH);
              ftr.score++;
              break makeEnmy;
            }
          }
        }

        // *** 自分の弾を発射 ***
        if (ftr.sflag == true && ftr.delaytime == 0) { // もしスペースキーが押されていて&待ち時間がゼロ
          for (i = 0; i < ftrBltNum; i++) { // 全部の弾に関して前から探査して
            if (ftrBlt[i].hp <= 0) { // 非アクティブの (死んでいる)弾があれば
              ftrBlt[i].revive(ftr.x, ftr.y); // 自機から弾を発射して
              ftr.delaytime = 5; //自機の弾発射待ち時間を元に戻して
              break; // for loop を抜ける
            }
          }
        } else if (ftr.delaytime > 0) { // 弾を発射しない(出来ない)場合は
          ftr.delaytime--; // 待ち時間を1減らす
        }

        // *** 各オブジェクト間の衝突チェック ***/
        for (i = 0; i < enmyAnum; i++) { // すべてのに関して
          if (enmyA[i].hp > 0) { // 敵が生きていたら
            ftr.collisionCheck(enmyA[i]); // 自機と衝突チェック
          }
          for (j = 0; j < ftrBltNum; j++) { // 全ての自弾に関して
            if (ftrBlt[j].hp > 0) { // 自弾が生きていたら
              ftrBlt[j].collisionCheck(enmyA[i]); // 自弾との衝突チェック
            }
          }
        }
        for (i = 0; i < enmyBnum; i++) { // すべてのに関して
          if (enmyB[i].hp > 0) { // 敵が生きていたら
            ftr.collisionCheck(enmyB[i]); // 自機と衝突チェック
          }
          for (j = 0; j < ftrBltNum; j++) { // 全ての自弾に関して
            if (ftrBlt[j].hp > 0) { // 自弾が生きていたら
              ftrBlt[j].collisionCheck(enmyB[i]); // 自弾との衝突チェック
            }
          }
        }

        // *** 自機の生死を判断 ***
        if (ftr.hp < 1) { // ゲーム終了
          mode = -2;
        }
        if (ftr.score >= 200) {
          mode = 1;
        }

        // *** オブジェクトの描画&移動 ***
        for (i = 0; i < enmyAnum; i++) {
          enmyA[i].move(buf_gc, imgW, imgH);
        }
        for (i = 0; i < enmyBnum; i++) {
          enmyB[i].move(buf_gc, imgW, imgH);
        }
        for (i = 0; i < ftrBltNum; i++) {
          ftrBlt[i].move(buf_gc, imgW, imgH);
        }
        ftr.move(buf_gc, imgW, imgH);

    }

    g.drawImage(buf, 0, 0, this); // 表の画用紙に患の画用紙 (buffer)の内容を貼り付ける
  }

  // ■ メソッド (Canvas)
  public void update(Graphics gc) { // repaint() に呼ばれる
    paint(gc);
  }

  // ■ メソッド (KeyListener)
  public void keyTyped(KeyEvent ke) {} // 今回は使わないが実装は必要

  public void keyPressed(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
      case KeyEvent.VK_LEFT: // [ー]キーが押されたら、
        ftr.lflag = true; // フラグを立てる
        break;
      case KeyEvent.VK_RIGHT: // [→]キーが押されたら
        ftr.rflag = true; // フラグを立てる
        break;
      case KeyEvent.VK_UP: // [↑]キーが押されたら
        ftr.uflag = true; // フラグを立てる
        break;
      case KeyEvent.VK_DOWN: // [↓]キーが押されたら
        ftr.dflag = true; // フラグを立てる
        break;
      case KeyEvent.VK_SPACE: // スペースキーが押されたら、
        ftr.sflag = true; // フラグを立てる
        if (this.mode == -2 || this.mode == -1) {
          this.mode++;
          ftr.hp = 10;
        } else if (this.mode == 1) {
          this.mode = 2;
          ftr.hp = 10;
        } else if (this.mode == 3) {
          this.mode = -1;
          ftr.hp = 10;
        }
        break;
    }
  }

  // ■ メソッド (KeyListener)
  public void keyReleased(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
      case KeyEvent.VK_LEFT: // [-]キーが離されたら、
        ftr.lflag = false; // フラグを降ろす
        break;
      case KeyEvent.VK_RIGHT: // [→]キーが離されたら
        ftr.rflag = false; // フラグを降ろす
        break;
      case KeyEvent.VK_UP: // [1]キーが離されたら
        ftr.uflag = false; // フラグを降ろす
        break;
      case KeyEvent.VK_DOWN: // [4]キーが離されたら
        ftr.dflag = false; // フラグを降ろす
        break;
      case KeyEvent.VK_SPACE: // スペースキーが離されたら
        ftr.sflag = false; // フラグを降ろす
        break;
    }
  }
}
