import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ReceiptMySuffixComponent } from './list/receipt-my-suffix.component';
import { ReceiptMySuffixDetailComponent } from './detail/receipt-my-suffix-detail.component';
import { ReceiptMySuffixUpdateComponent } from './update/receipt-my-suffix-update.component';
import ReceiptMySuffixResolve from './route/receipt-my-suffix-routing-resolve.service';

const receiptRoute: Routes = [
  {
    path: '',
    component: ReceiptMySuffixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReceiptMySuffixDetailComponent,
    resolve: {
      receipt: ReceiptMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReceiptMySuffixUpdateComponent,
    resolve: {
      receipt: ReceiptMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReceiptMySuffixUpdateComponent,
    resolve: {
      receipt: ReceiptMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default receiptRoute;
