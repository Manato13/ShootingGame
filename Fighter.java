/**
 * 自機のクラス
 *
 * 元ソースコード： @author fukai
 *改編者：中村真士：1203033116
 */


import java.awt.*;


//  画像を取り込むプログラム
class FighterImage extends Canvas {

  // ■ フィールド変数
  // Image クラスのオブジェクトに画像を取り込む
  // 実行ファイルのあるディレクトリ内の img ディレクトリにある画像を指定

  //敵Aの画像を取り込む
  Image fighterimg = getToolkit().getImage("img/fighter.png");

}

class Fighter extends MovingObject {

  boolean lflag; // ←が押されているかを記録
  boolean rflag; // →が押されているか記録
  boolean uflag; // ↑が押されているか記録
  boolean dflag; // ↓が押されているか記録
  boolean sflag; // スペースキーが押されているか記録
  int score; //点数を記録する変数
  int delaytime; // 打てる弾数を制限するためのカウント
  //boolean testflag;
  

  // コンストラクタ
  Fighter(int apWidth, int apHeight) {
    // super(); // 省略可

    //リスポーン地点の設定
    x = (int) (apWidth / 2); // 画面の真中
    y = (int) (apHeight * 0.9); // 画面の下の方
    dx = 7;
    dy = 7;
    w = 10;
    h = 10;

    //始めはフラグを下げておく
    lflag = false;
    rflag = false;
    uflag = false;
    dflag = false;
    sflag = false;

    delaytime = 5; // 弾の発射待ち時間
    score = 0; //始めは点数をoにしておく
  }

  void revive(int apWidth, int apHeight) {}

  void move(Graphics buf, int apWidth, int apHeight) {

    //ビジュアルを整える
      FighterImage fighterimgobj;
      fighterimgobj = new FighterImage();

      buf.drawImage(fighterimgobj.fighterimg, x-w, y-h,50,50,fighterimgobj);

    //挙動の調整
    if (lflag && !rflag && x > w) { // 左キーON, 右キーOFF, 左の壁にあたっていない場合
      x = x - dx; // オブジェクトを左に動かす
    }
    if (rflag && !lflag && x < apWidth - w) { // 右キーON, 左キーOFF
      x = x + dx; // オブジェクトを右に動かす
    }
    if (uflag && !dflag && y > h) {
      y = y - dy;
    }
    if (dflag && !uflag && y < apHeight - h) {
      y = y + dy;
    }
    // System.out.println("flags: " + lflag + rflag + delaytime ); // 状況チェック
    // System.out.println("(x, y)=(" + x + ", " + y + ")"); // 状態確認用
  }
}
