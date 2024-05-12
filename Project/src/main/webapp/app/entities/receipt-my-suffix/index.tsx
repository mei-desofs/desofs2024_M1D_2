import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReceiptMySuffix from './receipt-my-suffix';
import ReceiptMySuffixDetail from './receipt-my-suffix-detail';
import ReceiptMySuffixUpdate from './receipt-my-suffix-update';
import ReceiptMySuffixDeleteDialog from './receipt-my-suffix-delete-dialog';

const ReceiptMySuffixRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReceiptMySuffix />} />
    <Route path="new" element={<ReceiptMySuffixUpdate />} />
    <Route path=":id">
      <Route index element={<ReceiptMySuffixDetail />} />
      <Route path="edit" element={<ReceiptMySuffixUpdate />} />
      <Route path="delete" element={<ReceiptMySuffixDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReceiptMySuffixRoutes;
