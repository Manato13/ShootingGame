/**
 * ボスのクラス
 *
 * 元ソースコード： @author fukai
 *改編者：中村真士：1203033116
 */

import java.awt.*;


//  画像を取り込むプログラム
class BossImage extends Canvas {

  // ■ フィールド変数
  // Image クラスのオブジェクトに画像を取り込む
  // 実行ファイルのあるディレクトリ内の img ディレクトリにある画像を指定

  //Bossの画像を取り込む
  Image bossimg = getToolkit().getImage("img/boss.gif");

}

class Boss extends MovingObject {

  int delaytime;

  Boss(int apWidth, int apHeight) {
    super(apWidth, apHeight); // スーパークラス(ObjBase)のコンストラクタの呼び出し
    dx = (int) (Math.random() * 10 + 7); // ランダム
    dy = (int) (Math.random() * 10 + 7); // ランダム
    w = 100;
    h = 100;
    hp = 50; // 初期状態ではhp50
  
  }

  void move(Graphics buf, int apWidth, int apHeight) {
    buf.setColor(Color.red); //　gc の色を黒に
    if (hp > 0) { // もし生きていれば

  //bossをオブジェクト化して表示する
      BossImage bossimgobj;
      bossimgobj = new BossImage();

      buf.drawImage(bossimgobj.bossimg, x-w, y-h,2 * w,2 * h,bossimgobj);
      //buf.drawRect(x - w, y - h, 2 * w, 2 * h); // gc を使って■を描く


      x = x + dx; // 座標値の更新
      y = y + dy; // 座標値の更新
    }
    if (x - w <= 0) { //左端に当たった時の処理
      x = w;
      dx = -2 * dx;
     
    }
    if (x + w >= apWidth) { //右端に当たった時の処理
      x = apWidth - w;
      dx = -20;
      //dx = (int) (Math.random() * 10 - 25);
      //dy = (int) (Math.random() * 10);
    }
    if (y - h <= 0) { //上端に当たった時の処理
      y = h;
      dy = -1 * dy;
      //dx = (int) (Math.random() * 10);
      //dy = (int) (Math.random() * 10 + 5);
    }
    if (y + h >= apHeight) { //下端に当たった時の処理
      y = apHeight - h;
      dy = -1 * dy;
      //dx = (int) (Math.random() * 10);
      //dy = (int) (Math.random() * 10 - 25);
    }
  }

  void revive(int apWidth, int apHeight) {}
}
