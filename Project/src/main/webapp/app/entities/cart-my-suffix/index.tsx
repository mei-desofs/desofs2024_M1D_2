import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CartMySuffix from './cart-my-suffix';
import CartMySuffixDetail from './cart-my-suffix-detail';
import CartMySuffixUpdate from './cart-my-suffix-update';
import CartMySuffixDeleteDialog from './cart-my-suffix-delete-dialog';

const CartMySuffixRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CartMySuffix />} />
    <Route path="new" element={<CartMySuffixUpdate />} />
    <Route path=":id">
      <Route index element={<CartMySuffixDetail />} />
      <Route path="edit" element={<CartMySuffixUpdate />} />
      <Route path="delete" element={<CartMySuffixDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CartMySuffixRoutes;
