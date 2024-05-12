import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RoleUserMySuffix from './role-user-my-suffix';
import RoleUserMySuffixDetail from './role-user-my-suffix-detail';
import RoleUserMySuffixUpdate from './role-user-my-suffix-update';
import RoleUserMySuffixDeleteDialog from './role-user-my-suffix-delete-dialog';

const RoleUserMySuffixRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RoleUserMySuffix />} />
    <Route path="new" element={<RoleUserMySuffixUpdate />} />
    <Route path=":id">
      <Route index element={<RoleUserMySuffixDetail />} />
      <Route path="edit" element={<RoleUserMySuffixUpdate />} />
      <Route path="delete" element={<RoleUserMySuffixDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RoleUserMySuffixRoutes;
