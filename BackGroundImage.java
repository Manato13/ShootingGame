/**
 * 動かない画像を取り扱うクラス
 * 元ソースコード： @author fukai
 *改編者：中村真士：1203033116
 */


//  Image クラスは java.awt.Image をインポートする必要がある
import java.awt.*;

//  画像を取り込むプログラム
public class BackGroundImage extends Canvas {

  // ■ フィールド変数
  // Image クラスのオブジェクトに画像を取り込む
  // 実行ファイルのあるディレクトリ内の img ディレクトリにある画像を指定

  //通常ステージの背景
  Image mainstage = getToolkit().getImage("img/haikei.jpg");

  //スタート画面の背景
  Image start = getToolkit().getImage("img/start.jpg");

  //ボスバトルの背景
  Image bossbattle = getToolkit().getImage("img/bossbattle.jpg");
  

}