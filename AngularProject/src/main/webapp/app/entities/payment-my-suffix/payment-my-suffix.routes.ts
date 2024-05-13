import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PaymentMySuffixComponent } from './list/payment-my-suffix.component';
import { PaymentMySuffixDetailComponent } from './detail/payment-my-suffix-detail.component';
import { PaymentMySuffixUpdateComponent } from './update/payment-my-suffix-update.component';
import PaymentMySuffixResolve from './route/payment-my-suffix-routing-resolve.service';

const paymentRoute: Routes = [
  {
    path: '',
    component: PaymentMySuffixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentMySuffixDetailComponent,
    resolve: {
      payment: PaymentMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentMySuffixUpdateComponent,
    resolve: {
      payment: PaymentMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentMySuffixUpdateComponent,
    resolve: {
      payment: PaymentMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paymentRoute;
