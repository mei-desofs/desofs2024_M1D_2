import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CartMySuffixComponent } from './list/cart-my-suffix.component';
import { CartMySuffixDetailComponent } from './detail/cart-my-suffix-detail.component';
import { CartMySuffixUpdateComponent } from './update/cart-my-suffix-update.component';
import CartMySuffixResolve from './route/cart-my-suffix-routing-resolve.service';

const cartRoute: Routes = [
  {
    path: '',
    component: CartMySuffixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CartMySuffixDetailComponent,
    resolve: {
      cart: CartMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CartMySuffixUpdateComponent,
    resolve: {
      cart: CartMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CartMySuffixUpdateComponent,
    resolve: {
      cart: CartMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cartRoute;
