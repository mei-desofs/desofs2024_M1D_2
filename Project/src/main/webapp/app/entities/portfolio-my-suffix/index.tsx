import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PortfolioMySuffix from './portfolio-my-suffix';
import PortfolioMySuffixDetail from './portfolio-my-suffix-detail';
import PortfolioMySuffixUpdate from './portfolio-my-suffix-update';
import PortfolioMySuffixDeleteDialog from './portfolio-my-suffix-delete-dialog';

const PortfolioMySuffixRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PortfolioMySuffix />} />
    <Route path="new" element={<PortfolioMySuffixUpdate />} />
    <Route path=":id">
      <Route index element={<PortfolioMySuffixDetail />} />
      <Route path="edit" element={<PortfolioMySuffixUpdate />} />
      <Route path="delete" element={<PortfolioMySuffixDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PortfolioMySuffixRoutes;
