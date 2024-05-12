import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserMySuffix from './user-my-suffix';
import UserMySuffixDetail from './user-my-suffix-detail';
import UserMySuffixUpdate from './user-my-suffix-update';
import UserMySuffixDeleteDialog from './user-my-suffix-delete-dialog';

const UserMySuffixRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserMySuffix />} />
    <Route path="new" element={<UserMySuffixUpdate />} />
    <Route path=":id">
      <Route index element={<UserMySuffixDetail />} />
      <Route path="edit" element={<UserMySuffixUpdate />} />
      <Route path="delete" element={<UserMySuffixDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserMySuffixRoutes;
