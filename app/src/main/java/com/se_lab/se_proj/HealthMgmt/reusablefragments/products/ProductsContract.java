package com.se_lab.se_proj.HealthMgmt.reusablefragments.products;

import com.se_lab.se_proj.BasePresenter;
import com.se_lab.se_proj.BaseView;
import com.se_lab.se_proj.model.Product;

import java.util.List;

public interface ProductsContract {

    interface View extends BaseView<Presenter> {

        void sendClickedProduct(Product product);

        void updateProducts(List<Product> products);

        void updateQuery(String query);

        interface ProductsClickedCallback {
            void sendClickedProduct(Product product);
        }

    }

    interface Presenter extends BasePresenter {

        void getFood();

        void getQuery();

        void onQueryChange(String query);

        void onProductClicked(int position, Product product);

    }

    interface Model {

        void getProducts(GetProductsCallback callback);

        interface GetProductsCallback {
            void onProductsLoaded(List<Product> products);

            void onDataNotAvailable();
        }

        void getQuery(GetQueryCallback callback);

        void setQuery(String query);

        interface GetQueryCallback {
            void onQueryLoaded(String query);
        }

        void setChosenProduct(Product product);

    }
}
