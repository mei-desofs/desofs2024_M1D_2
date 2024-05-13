import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserMySuffixComponent } from './list/user-my-suffix.component';
import { UserMySuffixDetailComponent } from './detail/user-my-suffix-detail.component';
import { UserMySuffixUpdateComponent } from './update/user-my-suffix-update.component';
import UserMySuffixResolve from './route/user-my-suffix-routing-resolve.service';

const userRoute: Routes = [
  {
    path: '',
    component: UserMySuffixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserMySuffixDetailComponent,
    resolve: {
      user: UserMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserMySuffixUpdateComponent,
    resolve: {
      user: UserMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserMySuffixUpdateComponent,
    resolve: {
      user: UserMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userRoute;
