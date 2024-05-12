import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CartMySuffix from './cart-my-suffix';
import PaymentMySuffix from './payment-my-suffix';
import PhotoMySuffix from './photo-my-suffix';
import PortfolioMySuffix from './portfolio-my-suffix';
import ReceiptMySuffix from './receipt-my-suffix';
import RoleMySuffix from './role-my-suffix';
import RoleUserMySuffix from './role-user-my-suffix';
import UserMySuffix from './user-my-suffix';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="cart-my-suffix/*" element={<CartMySuffix />} />
        <Route path="payment-my-suffix/*" element={<PaymentMySuffix />} />
        <Route path="photo-my-suffix/*" element={<PhotoMySuffix />} />
        <Route path="portfolio-my-suffix/*" element={<PortfolioMySuffix />} />
        <Route path="receipt-my-suffix/*" element={<ReceiptMySuffix />} />
        <Route path="role-my-suffix/*" element={<RoleMySuffix />} />
        <Route path="role-user-my-suffix/*" element={<RoleUserMySuffix />} />
        <Route path="user-my-suffix/*" element={<UserMySuffix />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
