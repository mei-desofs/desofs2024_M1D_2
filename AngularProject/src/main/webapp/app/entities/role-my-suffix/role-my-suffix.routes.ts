import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RoleMySuffixComponent } from './list/role-my-suffix.component';
import { RoleMySuffixDetailComponent } from './detail/role-my-suffix-detail.component';
import { RoleMySuffixUpdateComponent } from './update/role-my-suffix-update.component';
import RoleMySuffixResolve from './route/role-my-suffix-routing-resolve.service';

const roleRoute: Routes = [
  {
    path: '',
    component: RoleMySuffixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoleMySuffixDetailComponent,
    resolve: {
      role: RoleMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoleMySuffixUpdateComponent,
    resolve: {
      role: RoleMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoleMySuffixUpdateComponent,
    resolve: {
      role: RoleMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default roleRoute;
