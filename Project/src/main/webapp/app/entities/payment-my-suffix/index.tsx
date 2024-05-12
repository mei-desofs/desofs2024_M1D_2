import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentMySuffix from './payment-my-suffix';
import PaymentMySuffixDetail from './payment-my-suffix-detail';
import PaymentMySuffixUpdate from './payment-my-suffix-update';
import PaymentMySuffixDeleteDialog from './payment-my-suffix-delete-dialog';

const PaymentMySuffixRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentMySuffix />} />
    <Route path="new" element={<PaymentMySuffixUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentMySuffixDetail />} />
      <Route path="edit" element={<PaymentMySuffixUpdate />} />
      <Route path="delete" element={<PaymentMySuffixDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentMySuffixRoutes;
