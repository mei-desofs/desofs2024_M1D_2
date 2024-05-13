import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoleUserMySuffix, NewRoleUserMySuffix } from '../role-user-my-suffix.model';

export type PartialUpdateRoleUserMySuffix = Partial<IRoleUserMySuffix> & Pick<IRoleUserMySuffix, 'id'>;

export type EntityResponseType = HttpResponse<IRoleUserMySuffix>;
export type EntityArrayResponseType = HttpResponse<IRoleUserMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class RoleUserMySuffixService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/role-users');

  create(roleUser: NewRoleUserMySuffix): Observable<EntityResponseType> {
    return this.http.post<IRoleUserMySuffix>(this.resourceUrl, roleUser, { observe: 'response' });
  }

  update(roleUser: IRoleUserMySuffix): Observable<EntityResponseType> {
    return this.http.put<IRoleUserMySuffix>(`${this.resourceUrl}/${this.getRoleUserMySuffixIdentifier(roleUser)}`, roleUser, {
      observe: 'response',
    });
  }

  partialUpdate(roleUser: PartialUpdateRoleUserMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IRoleUserMySuffix>(`${this.resourceUrl}/${this.getRoleUserMySuffixIdentifier(roleUser)}`, roleUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoleUserMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoleUserMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoleUserMySuffixIdentifier(roleUser: Pick<IRoleUserMySuffix, 'id'>): number {
    return roleUser.id;
  }

  compareRoleUserMySuffix(o1: Pick<IRoleUserMySuffix, 'id'> | null, o2: Pick<IRoleUserMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoleUserMySuffixIdentifier(o1) === this.getRoleUserMySuffixIdentifier(o2) : o1 === o2;
  }

  addRoleUserMySuffixToCollectionIfMissing<Type extends Pick<IRoleUserMySuffix, 'id'>>(
    roleUserCollection: Type[],
    ...roleUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roleUsers: Type[] = roleUsersToCheck.filter(isPresent);
    if (roleUsers.length > 0) {
      const roleUserCollectionIdentifiers = roleUserCollection.map(roleUserItem => this.getRoleUserMySuffixIdentifier(roleUserItem));
      const roleUsersToAdd = roleUsers.filter(roleUserItem => {
        const roleUserIdentifier = this.getRoleUserMySuffixIdentifier(roleUserItem);
        if (roleUserCollectionIdentifiers.includes(roleUserIdentifier)) {
          return false;
        }
        roleUserCollectionIdentifiers.push(roleUserIdentifier);
        return true;
      });
      return [...roleUsersToAdd, ...roleUserCollection];
    }
    return roleUserCollection;
  }
}
