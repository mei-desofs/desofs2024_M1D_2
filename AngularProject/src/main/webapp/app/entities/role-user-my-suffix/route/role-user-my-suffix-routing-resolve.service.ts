import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoleUserMySuffix } from '../role-user-my-suffix.model';
import { RoleUserMySuffixService } from '../service/role-user-my-suffix.service';

const roleUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IRoleUserMySuffix> => {
  const id = route.params['id'];
  if (id) {
    return inject(RoleUserMySuffixService)
      .find(id)
      .pipe(
        mergeMap((roleUser: HttpResponse<IRoleUserMySuffix>) => {
          if (roleUser.body) {
            return of(roleUser.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default roleUserResolve;
