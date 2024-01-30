/**
 * 敵Aのクラス
 *
 *元ソースコード： @author fukai
 *改編者：中村真士：1203033116
 */

import java.awt.*;


//  画像を取り込むプログラム
class EnemyAImage extends Canvas {

  // ■ フィールド変数
  // Image クラスのオブジェクトに画像を取り込む
  // 実行ファイルのあるディレクトリ内の img ディレクトリにある画像を指定

  //敵Aの画像を取り込む
  Image enemyaimg = getToolkit().getImage("img/EnemyA001.png");

}

class EnemyA extends MovingObject {

  // コンストラクタ(初期値設定)
  EnemyA(int apWidth, int apHeight) {
    super(apWidth, apHeight); // スーパークラス(ObjBase)のコンストラクタの呼び出し
    w = 12;
    h = 12;
    hp = 0; // 初期状態では全て死亡
  }

  // 敵Aを描き更新するメソッド
  void move(Graphics buf, int apWidth, int apHeight) {
    buf.setColor(Color.red); //　gc の色を赤に
    if (hp > 0) { // もし生きていれば

    //敵Aをオブジェクト化して表示する
      EnemyAImage enemyaimgobj;
      enemyaimgobj = new EnemyAImage();

      buf.drawImage(enemyaimgobj.enemyaimg, x-w, y-h,100,100,enemyaimgobj);

      if (Math.random() < 0.1){
        dx = (int)(dx * Math.random()*1.5);
        dy = (int)(dy * Math.random()*1.5); 
      }

      if (dx == 0){
        dx = (int)(Math.random()*5 - 2.5);
      }

      if (dy == 0){
        dy = (int)(Math.random()*5);
      }

      x = x + dx; // 座標値の更新
      y = y + dy; // 座標値の更新
      
      if (y > apHeight + h) {
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
