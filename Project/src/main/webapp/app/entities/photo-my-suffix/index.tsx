import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PhotoMySuffix from './photo-my-suffix';
import PhotoMySuffixDetail from './photo-my-suffix-detail';
import PhotoMySuffixUpdate from './photo-my-suffix-update';
import PhotoMySuffixDeleteDialog from './photo-my-suffix-delete-dialog';

const PhotoMySuffixRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PhotoMySuffix />} />
    <Route path="new" element={<PhotoMySuffixUpdate />} />
    <Route path=":id">
      <Route index element={<PhotoMySuffixDetail />} />
      <Route path="edit" element={<PhotoMySuffixUpdate />} />
      <Route path="delete" element={<PhotoMySuffixDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PhotoMySuffixRoutes;
