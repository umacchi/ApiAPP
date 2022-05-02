package jp.techacademy.hide.yui.apiapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

// Squareが開発した画像ライブラリ
// Adapter内でのImageViewのリサイクルやダウンロード処理のキャンセルを自動で実行してくれる
// 複雑な画像の変形処理を最小のメモリ消費で実現できる
// 自動でメモリ・ファイルキャッシュをやってくれる
import com.squareup.picasso.Picasso

class FavoriteAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    // お気に入り登録したShopを格納
    private val items = mutableListOf<FavoriteShop>()

    // お気に入り画面から削除するときのコールバック（ApiFragmentへ通知するメソッド)
    var onClickDeleteFavorite: ((FavoriteShop) -> Unit)? = null

    // Itemを押したときのメソッド
    var onClickItem: ((String) -> Unit)? = null

    // お気に入り画面用のViewHolderオブジェクトの生成
    // このメソッドは、1件分のデータを生成し、返すメソッドでした。
    // つまり、1行分にあるUI部品に該当のデータをセットする必要がある
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // ViewTypeに応じて、生成されるViewHolderを出し分ける
        return when(viewType) {
            // ViewTypeがVIEW_TYPE_EMPTY（つまり、お気に入り登録が0件）の場合
            VIEW_TYPE_EMPTY -> EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_favorite_empty, parent, false))
            // 上記以外（つまり、1件以上のお気に入りが登録されている場合
            else -> FavoriteItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_favorite, parent, false))
        }
    }

    // 更新用のメソッド
    fun refresh(list: List<FavoriteShop>) {
        items.apply {
            clear() // items を 空にする
            addAll(list) // itemsにlistを全て追加する
        }
        notifyDataSetChanged() // recyclerViewを再描画させる
    }

    // 表示させる件数を返す
    // お気に入りが1件もない時に、「お気に入りはありません」を出すため
    override fun getItemCount(): Int {
        return if (items.isEmpty()) 1 else items.size
    }

    // onCreateViewHolderの第二引数はここで決める。条件によってViewTypeを返すようにすると、一つのRecyclerViewで様々なViewがある物が作れる
    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    // onCreateViewHolder() メソッドによって生成された1件分を実際にバインドするメソッド
    // このとき、バインドする行は、お気に入りに1件以上登録されている、つまりFavoriteItemViewHolderオブジェクトかどうかの判定を行います
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // バインドされるFavoriteItemViewHolder内にあるfavoriteImageViewにイベントリスナーの登録、
        // さらにApiFragmentへのコールバック登録を行っています。
        // なお、コールバックの登録に必要となるプロパティの宣言も行っています
        if (holder is FavoriteItemViewHolder) {
            updateFavoriteItemViewHolder(holder, position)
        }
    }

    // ViewHolder内のUI部品に値などをセット
    private fun updateFavoriteItemViewHolder(holder: FavoriteItemViewHolder, position: Int) {
        val data = items[position]
        holder.apply {
            rootView.apply {
                // 偶数番目と機数番目で背景色を変更させる
                setBackgroundColor(ContextCompat.getColor(context, if (position % 2 == 0) android.R.color.white else android.R.color.darker_gray))
                setOnClickListener {
                    onClickItem?.invoke(data.url)
                }
            }
            nameTextView.text = data.name
            Picasso.get().load(data.imageUrl).into(imageView) // Picassoというライブラリを使ってImageVIewに画像をはめ込む
            favoriteImageView.setOnClickListener {
                onClickDeleteFavorite?.invoke(data)
                notifyItemChanged(position)
            }
        }
    }

    // お気に入りが登録されているときのリスト
    // お気に入りに登録されているShopが1件以上あるときに利用するレイアウトファイルとの連携するクラス
    // この時、バインドする行はお気に入りに1件以上登録されている、つまりFavoriteItemViewHolderオブジェクトかどうかの判定を行う
    class FavoriteItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val rootView : ConstraintLayout = view.findViewById(R.id.rootView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val favoriteImageView: ImageView = view.findViewById(R.id.favoriteImageView)
    }

    // お気に入り登録がまだ行われていないとき
    // お気に入りに登録されているShopがないときに利用するレイアウトファイルとの連携するクラス
    class EmptyViewHolder(view: View): RecyclerView.ViewHolder(view)

    companion object {
        // Viewの種類を表現する定数、こちらはお気に入りのお店
        private const val VIEW_TYPE_ITEM = 0
        // Viewの種類を表現する定数、こちらはお気に入りが１件もないとき
        private const val VIEW_TYPE_EMPTY = 1
    }
}