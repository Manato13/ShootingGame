/**
 * 敵Bのクラス
 *
 * 元ソースコード： @author fukai
 *改編者：中村真士：1203033116
 */

import java.awt.*;


//  画像を取り込むプログラム
class EnemyBImage extends Canvas {

  // ■ フィールド変数
  // Image クラスのオブジェクトに画像を取り込む
  // 実行ファイルのあるディレクトリ内の img ディレクトリにある画像を指定

  //敵Bの画像を取り込む
  Image enemybimg001 = getToolkit().getImage("img/EnemyB.gif");
  //Image enemybimg002 = getToolkit().getImage("img/EnemyB002.png");

}

public class EnemyB extends MovingObject {

  // コンストラクタ(初期値設定)
  EnemyB(int apWidth, int apHeight) {
    super(apWidth, apHeight); // スーパークラス(ObjBase)のコンストラクタの呼び出し
    w = 12;
    h = 12;
    hp = 0; // 初期状態では全て死亡
  }

  // 敵Bを描き更新するメソッド
  void move(Graphics buf, int apWidth, int apHeight) {
    buf.setColor(Color.black); //　gc の色を黒に
    if (hp > 0) { // もし生きていれば

    //敵Bをオブジェクト化して表示する
      EnemyBImage enemybimg001obj;
      //EnemyBImage enemybimg002obj;
      enemybimg001obj = new EnemyBImage();
      //enemybimg002obj = new EnemyBImage();

      buf.drawImage(enemybimg001obj.enemybimg001, x-w, y-h,100,100,enemybimg001obj);
      
      //buf.fillOval(x - w, y - h, 2 * w, 2 * h); // gc を使って●を描く
      
    if (x >= apWidth)  dx=dx*(-1)*10;     // 右端に当たったときの処理
    if (x <= 0)      dx=dx*(-1)*10;     // 左端に当たったときの処理
    if (y >= apHeight) dy=dy*(-1)*10;     // 下端に当たったときの処理
      
      x = x + dx; // 座標値の更新
      y = y + dy; // 座標値の更新
      
      if (dy <= 0 && y <= -10) {
        hp = 0;
      }
    }
  }

  void revive(int apWidth, int apHeight) { // 敵を新たに生成 (再利用)
    x = (int) (Math.random() * (apWidth - 2 * w) + w);
    y = -h;
    dy = 1;
    if (x < apWidth / 2) {
      dx = (int) (Math.random() * 2);
    } else {
      dx = -(int) (Math.random() * 2);
    }
    hp = 1;
  }
}
