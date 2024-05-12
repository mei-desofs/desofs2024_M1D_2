import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RoleMySuffix from './role-my-suffix';
import RoleMySuffixDetail from './role-my-suffix-detail';
import RoleMySuffixUpdate from './role-my-suffix-update';
import RoleMySuffixDeleteDialog from './role-my-suffix-delete-dialog';

const RoleMySuffixRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RoleMySuffix />} />
    <Route path="new" element={<RoleMySuffixUpdate />} />
    <Route path=":id">
      <Route index element={<RoleMySuffixDetail />} />
      <Route path="edit" element={<RoleMySuffixUpdate />} />
      <Route path="delete" element={<RoleMySuffixDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RoleMySuffixRoutes;
