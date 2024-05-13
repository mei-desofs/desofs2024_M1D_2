import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoleMySuffix, NewRoleMySuffix } from '../role-my-suffix.model';

export type PartialUpdateRoleMySuffix = Partial<IRoleMySuffix> & Pick<IRoleMySuffix, 'id'>;

export type EntityResponseType = HttpResponse<IRoleMySuffix>;
export type EntityArrayResponseType = HttpResponse<IRoleMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class RoleMySuffixService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/roles');

  create(role: NewRoleMySuffix): Observable<EntityResponseType> {
    return this.http.post<IRoleMySuffix>(this.resourceUrl, role, { observe: 'response' });
  }

  update(role: IRoleMySuffix): Observable<EntityResponseType> {
    return this.http.put<IRoleMySuffix>(`${this.resourceUrl}/${this.getRoleMySuffixIdentifier(role)}`, role, { observe: 'response' });
  }

  partialUpdate(role: PartialUpdateRoleMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IRoleMySuffix>(`${this.resourceUrl}/${this.getRoleMySuffixIdentifier(role)}`, role, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoleMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoleMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoleMySuffixIdentifier(role: Pick<IRoleMySuffix, 'id'>): number {
    return role.id;
  }

  compareRoleMySuffix(o1: Pick<IRoleMySuffix, 'id'> | null, o2: Pick<IRoleMySuffix, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoleMySuffixIdentifier(o1) === this.getRoleMySuffixIdentifier(o2) : o1 === o2;
  }

  addRoleMySuffixToCollectionIfMissing<Type extends Pick<IRoleMySuffix, 'id'>>(
    roleCollection: Type[],
    ...rolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roles: Type[] = rolesToCheck.filter(isPresent);
    if (roles.length > 0) {
      const roleCollectionIdentifiers = roleCollection.map(roleItem => this.getRoleMySuffixIdentifier(roleItem));
      const rolesToAdd = roles.filter(roleItem => {
        const roleIdentifier = this.getRoleMySuffixIdentifier(roleItem);
        if (roleCollectionIdentifiers.includes(roleIdentifier)) {
          return false;
        }
        roleCollectionIdentifiers.push(roleIdentifier);
        return true;
      });
      return [...rolesToAdd, ...roleCollection];
    }
    return roleCollection;
  }
}
