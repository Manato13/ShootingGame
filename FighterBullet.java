/** 
* 自機から発射する弾のクラス
*  
*元ソースコード： @author fukai
 *改編者：中村真士：1203033116
*/

import java.awt.*;

//  画像を取り込むプログラム
class FighterBulletImage extends Canvas {

  // ■ フィールド変数
  // Image クラスのオブジェクトに画像を取り込む
  // 実行ファイルのあるディレクトリ内の img ディレクトリにある画像を指定

  //発射する弾の画像を取り込む
  Image fighterbulletimg = getToolkit().getImage("img/FighterBullet.png");

}


public class FighterBullet extends MovingObject {

  /** コンストラクタ **/
  FighterBullet() {
    w = h = 3; // 弾の半径
    dx = 0;
    dy = -6;
    hp = 0; // 初期値は全て非アクティブ
    
  }



  /** メソッド **/
  void move(Graphics buf, int apWidth, int apHeight) {
    if (hp > 0) {

      //自機の弾をオブジェクト化して表示する
      FighterBulletImage fighterbulletimgobj;
      fighterbulletimgobj = new FighterBulletImage();
      buf.drawImage(fighterbulletimgobj.fighterbulletimg, x-w-5, y-h,50,50,fighterbulletimgobj);

      //buf.setColor(Color.black); // gc の色を黒に
      //buf.fillOval(x - w, y - h, 2 * w, 2 * h); // gc を使って・を描く、

      if (y > 0 && y < apHeight && x > 0 && x < apWidth) { // もし弾が画面内なら
        y = y + dy; // 弾の位置を更新
      } else {
        hp = 0; // 画面の外に出たら hp をゼロに
      }
    }
  }

  void revive(int x, int y) { // x, y はFighterの位置
    this.x = x;
    this.y = y;
    hp = 1; // 発射したら弾をアクティブにする
  }
}
