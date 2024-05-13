import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RoleUserMySuffixComponent } from './list/role-user-my-suffix.component';
import { RoleUserMySuffixDetailComponent } from './detail/role-user-my-suffix-detail.component';
import { RoleUserMySuffixUpdateComponent } from './update/role-user-my-suffix-update.component';
import RoleUserMySuffixResolve from './route/role-user-my-suffix-routing-resolve.service';

const roleUserRoute: Routes = [
  {
    path: '',
    component: RoleUserMySuffixComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoleUserMySuffixDetailComponent,
    resolve: {
      roleUser: RoleUserMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoleUserMySuffixUpdateComponent,
    resolve: {
      roleUser: RoleUserMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoleUserMySuffixUpdateComponent,
    resolve: {
      roleUser: RoleUserMySuffixResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default roleUserRoute;
