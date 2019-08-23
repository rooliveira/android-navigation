package br.com.alura.aluraesporte.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.alura.aluraesporte.R
import br.com.alura.aluraesporte.ui.fragment.DetalhesProdutoFragment
import br.com.alura.aluraesporte.ui.fragment.ListaProdutosFragment
import br.com.alura.aluraesporte.ui.fragment.PagamentoFragment
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.android.inject

private const val COMPRA_REALIZADA = "Compra realizada"

class ProdutosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.produtos_activity)
        if (savedInstanceState == null) {
            val produtosFragment: ListaProdutosFragment by inject()
            transacaoFragment {
                replace(R.id.container, produtosFragment)
            }
            FirebaseAnalytics.getInstance(this).setCurrentScreen(this, "vitrine", "VitrineActivity")
            val bundle = Bundle()
            bundle.putString("screen_name", "vitrine")
            FirebaseAnalytics.getInstance(this).logEvent("screenView", bundle)
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is ListaProdutosFragment -> {
                fragment.quandoProdutoSelecionado = { produtoSelecionado ->
                    val detalhesProdutoFragment: DetalhesProdutoFragment by inject()
                    val argumentos = Bundle()
                    argumentos.putLong(CHAVE_PRODUTO_ID, produtoSelecionado.id)
                    detalhesProdutoFragment.arguments = argumentos
                    transacaoFragment {
                        addToBackStack(null)
                        replace(R.id.container, detalhesProdutoFragment)
                    }
                    FirebaseAnalytics.getInstance(this).setCurrentScreen(this, "detalhes_produto", "ProdutosActivity")

                    val bundle = Bundle()
                    bundle.putString("screen_name", "detalhes dos produtos")
                    FirebaseAnalytics.getInstance(this).logEvent("screenView", bundle)

                    val bundle2 = Bundle()
                    bundle2.putString(FirebaseAnalytics.Param.ITEM_ID, produtoSelecionado.id.toString())
                    bundle2.putString(FirebaseAnalytics.Param.ITEM_NAME, produtoSelecionado.nome)
                    FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle2)


                    val product1 = Bundle()
                    product1.putString(FirebaseAnalytics.Param.ITEM_ID, "sku1234") // ITEM_ID or ITEM_NAME is required
                    product1.putString(FirebaseAnalytics.Param.ITEM_NAME, "Donut Friday Scented T-Shirt")
                    product1.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Apparel/Men/Shirts")
                    product1.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "Blue")
                    product1.putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
                    product1.putDouble(FirebaseAnalytics.Param.PRICE, 29.99)
                    product1.putString(FirebaseAnalytics.Param.CURRENCY, "USD") // Item-level currency unused today
                    product1.putLong(FirebaseAnalytics.Param.INDEX, 1) // Position of the item in the list


                    val ecommerceBundle = Bundle()
                    ecommerceBundle.putBundle("items", product1)

                    ecommerceBundle.putString(
                        FirebaseAnalytics.Param.ITEM_LIST,
                        "Vitrine"
                    ) // Optional list name

                    FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle)
                }
            }
            is DetalhesProdutoFragment -> {
                fragment.quandoProdutoComprado = { produtoComprado ->
                    val pagamentoFragment: PagamentoFragment by inject()
                    val dado = Bundle()
                    dado.putLong(CHAVE_PRODUTO_ID, produtoComprado.id)
                    pagamentoFragment.arguments = dado
                    transacaoFragment {
                        addToBackStack(null)
                        replace(R.id.container, pagamentoFragment)
                    }

                    FirebaseAnalytics.getInstance(this).setCurrentScreen(this, "pagamento", "PagamentoActivity")

                    val bundle = Bundle()
                    bundle.putString("screen_name", "pagamento")
                    FirebaseAnalytics.getInstance(this).logEvent("screenView", bundle)

                    val bundle2 = Bundle()
                    bundle.putString("screen_name", "pagamento")
                    FirebaseAnalytics.getInstance(this).logEvent("screenView", bundle2)

                }

            }
            is PagamentoFragment -> {
                fragment.quandoPagamentoRealizado = {
                    Toast.makeText(this, COMPRA_REALIZADA, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
